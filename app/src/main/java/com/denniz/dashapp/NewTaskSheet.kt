package com.denniz.dashapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denniz.dashapp.databinding.FragmentNewTaskSheetBinding
import com.denniz.dashapp.databinding.FragmentTodoListBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class NewTaskSheet() : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentNewTaskSheetBinding
    private lateinit var binding1: FragmentTodoListBinding
    private lateinit var todoDao: TodoDao

    fun setTodoDao(todoDao: TodoDao) {
        this.todoDao = todoDao
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNewTaskSheetBinding.inflate(inflater, container, false)
        binding1 = FragmentTodoListBinding.inflate(inflater, container, false)

        val recyclerView: RecyclerView = binding1.todoListRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val adapter = TodoAdapter(listOf())
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            val todoItems = withContext(Dispatchers.IO) {
                todoDao.getAll()
            }
        }

        binding.addNewTaskBtn.setOnClickListener{
            val titleEditText: EditText = binding.textInputTodo
            val title = titleEditText.text.toString()

            if (title.isNotEmpty()){
                val item = TodoItem(title = title)
                GlobalScope.launch {
                    insertItem(todoDao, item)
                }
                titleEditText.text.clear()
            }
            dismiss()
        }

        return binding.root
    }
    suspend fun insertItem(todoDao: TodoDao, item: TodoItem) {
        withContext(Dispatchers.IO) {
            todoDao.insert(item)
        }
    }
}