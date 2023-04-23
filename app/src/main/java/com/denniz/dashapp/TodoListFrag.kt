package com.denniz.dashapp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.denniz.dashapp.databinding.FragmentNewTaskSheetBinding
import com.denniz.dashapp.databinding.FragmentTodoItemBinding
import com.denniz.dashapp.databinding.FragmentTodoListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*


class TodoListFrag : Fragment() {
    private lateinit var binding: FragmentTodoListBinding
    private lateinit var bindingNewTaskSheetBinding: FragmentNewTaskSheetBinding
    private lateinit var bindingTodoItem: FragmentTodoItemBinding
    private var todoDao: TodoDao? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val database = Room.databaseBuilder(
            context.applicationContext,
            TodoDatabase::class.java, "todo_database"
        )   .fallbackToDestructiveMigration()
            .build()
        todoDao = database.todoDao()
    }
    @SuppressLint("MissingInflatedId", "FragmentLiveDataObserve")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoListBinding.inflate(inflater, container, false)
        bindingNewTaskSheetBinding = FragmentNewTaskSheetBinding.inflate(inflater, container, false)
        bindingTodoItem = FragmentTodoItemBinding.inflate(inflater, container, false)
        val coroutineScope = CoroutineScope(Dispatchers.Main)
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        val userID = auth.currentUser!!.uid

        coroutineScope.launch(Dispatchers.IO) {
            if (isAdded && todoDao == null) {
                val database = Room.databaseBuilder(
                    requireContext(),
                    TodoDatabase::class.java, "todo_database"
                ).build()

                todoDao = database.todoDao()
            }
            withContext(Dispatchers.Main) {
                val recyclerView: RecyclerView = binding.todoListRecyclerView
                recyclerView.layoutManager = LinearLayoutManager(requireContext())

                val adapter = TodoAdapter(listOf(), todoDao!!, this@TodoListFrag)
                recyclerView.adapter = adapter

                lifecycleScope.launch {
                    val todoItems = withContext(Dispatchers.IO) {
                        todoDao?.getAll() ?: emptyList()
                    }
                    adapter.updateItems(todoItems)
                }
            }
        }

        binding.newTaskBtn.setOnClickListener {
            val newTaskSheet = NewTaskSheet.newInstance(todoDao!!, this)
            newTaskSheet.setTodoDao(todoDao!!) // pass the todoDao to the NewTaskSheet
            newTaskSheet.show(parentFragmentManager, "newTaskTag")
        }
        binding.backBtnTasks.setOnClickListener {
            val bundle = bundleOf("fromFragment" to "weatherFrag")
            Navigation.findNavController(binding.root).navigate(R.id.action_todoListFrag_to_dashboardFrag, bundle)

        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateView()
    }
    fun updateView() {
        binding.let {
            lifecycleScope.launch {
                val todoItems = withContext(Dispatchers.IO) {
                    todoDao?.getAll() ?: emptyList()
                }
                binding.todoListRecyclerView.adapter?.let {
                    if (it is TodoAdapter) {
                        it.updateItems(todoItems)
                    }
                }
            }

        }
    }
}



