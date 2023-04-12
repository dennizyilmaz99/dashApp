package com.denniz.dashapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.denniz.dashapp.databinding.FragmentTodoListBinding

class TodoListFrag : Fragment() {
    private lateinit var binding: FragmentTodoListBinding
    @SuppressLint("MissingInflatedId", "FragmentLiveDataObserve")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTodoListBinding.inflate(inflater, container, false)
        binding.newTaskBtn.setOnClickListener() {
            NewTaskSheet().show(parentFragmentManager, "newTaskTag")
        }
        binding.backBtnTasks.setOnClickListener(){
            Navigation.findNavController(binding.root).navigate(R.id.action_todoListFrag_to_dashboardFrag)
        }

        return binding.root
    }
}



