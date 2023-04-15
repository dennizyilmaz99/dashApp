package com.denniz.dashapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.denniz.dashapp.databinding.FragmentTodoListBinding
import kotlinx.coroutines.*


class TodoListFrag : Fragment() {
    private lateinit var binding: FragmentTodoListBinding
    private lateinit var todoDao: TodoDao

    @SuppressLint("MissingInflatedId", "FragmentLiveDataObserve")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoListBinding.inflate(inflater, container, false)
        val coroutineScope = CoroutineScope(Dispatchers.Main)

        coroutineScope.launch(Dispatchers.IO) {
            val database = Room.databaseBuilder(
                requireContext(),
                TodoDatabase::class.java, "todo_database"
            ).build()

            todoDao = database.todoDao()

            withContext(Dispatchers.Main) {
                val recyclerView: RecyclerView = binding.todoListRecyclerView
                recyclerView.layoutManager = LinearLayoutManager(requireContext())

                val adapter = TodoAdapter(listOf())
                recyclerView.adapter = adapter

                lifecycleScope.launch {
                    val todoItems = withContext(Dispatchers.IO) {
                        todoDao.getAll()
                    }
                    adapter.updateItems(todoItems)
                }
            }
        }

        binding.newTaskBtn.setOnClickListener() {
            val newTaskSheet = NewTaskSheet()
            newTaskSheet.setTodoDao(todoDao) // pass the todoDao to the NewTaskSheet
            newTaskSheet.show(parentFragmentManager, "newTaskTag")
        }
        binding.backBtnTasks.setOnClickListener() {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_todoListFrag_to_dashboardFrag)
        }

        return binding.root
    }
    fun updateView() {
        lifecycleScope.launch {
            val todoItems = withContext(Dispatchers.IO) {
                todoDao.getAll()
            }
            binding.todoListRecyclerView.adapter?.let {
                if (it is TodoAdapter) {
                    it.updateItems(todoItems)
                }
            }
        }
    }
}



