package com.denniz.dashapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denniz.dashapp.databinding.FragmentNewTaskSheetBinding
import com.denniz.dashapp.databinding.FragmentTodoListBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.apphosting.datastore.testing.DatastoreTestTrace.FirestoreV1ActionOrBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class NewTaskSheet(private var todoListFrag: TodoListFrag) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentNewTaskSheetBinding
    private lateinit var bindingTodoList: FragmentTodoListBinding
    private lateinit var todoDao: TodoDao
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    companion object {
        fun newInstance(todoDao: TodoDao, todoListFrag: TodoListFrag): NewTaskSheet{
            return NewTaskSheet(todoListFrag).apply {
                this.todoDao = todoDao
                this.todoListFrag = todoListFrag
            }
        }
    }

    fun setTodoDao(todoDao: TodoDao) {
        this.todoDao = todoDao
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNewTaskSheetBinding.inflate(inflater, container, false)
        bindingTodoList = FragmentTodoListBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        val textInputLayoutTodo = binding.textInputLayoutTodo
        val currentUser = auth.currentUser

        val recyclerView: RecyclerView = bindingTodoList.todoListRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val adapter = TodoAdapter(listOf(), todoDao, todoListFrag)
        recyclerView.adapter = adapter

        binding.addNewTaskBtn.setOnClickListener{
            val titleEditText: EditText = binding.textInputTodo
            val title = titleEditText.text.toString()

            if (title.isNotEmpty()){
                val item = Todo(title = title)
                GlobalScope.launch {
                    insertItem(todoDao, item)
                    todoListFrag.updateView()
                    val user: MutableMap<String, Any> = HashMap()
                    user["Task"] = title
                    user["userID"] = currentUser!!.uid
                    db.collection("task_items")
                        .add(user)
                        .addOnSuccessListener { documentReference ->
                            Log.d(
                                ContentValues.TAG,
                                "DocumentSnapshot added with ID:"
                            )
                        }
                        .addOnFailureListener { e ->
                            Log.w(
                                ContentValues.TAG,
                                "Error adding document",
                                e
                            )
                        }
                }
                titleEditText.text.clear()
                dismiss()
            } else if (title.isEmpty()){
                val text = "Empty field. Please try again."
                val colorStateList = ColorStateList(
                    arrayOf(
                        intArrayOf(android.R.attr.state_focused),
                        intArrayOf(-android.R.attr.state_focused)
                    ),
                    intArrayOf(
                        ContextCompat.getColor(requireContext(), R.color.colorError),
                        ContextCompat.getColor(requireContext(), R.color.colorError)
                    ))
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
                textInputLayoutTodo.defaultHintTextColor = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.colorError))
                textInputLayoutTodo.setBoxStrokeColorStateList(colorStateList)
            }
        }

        return binding.root
    }
    suspend fun insertItem(todoDao: TodoDao, item: Todo) {
        withContext(Dispatchers.IO) {
            todoDao.insert(item)
        }
    }
}