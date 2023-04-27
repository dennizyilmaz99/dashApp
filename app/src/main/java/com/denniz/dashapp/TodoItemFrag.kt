package com.denniz.dashapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.denniz.dashapp.databinding.FragmentTodoItemBinding

class TodoItemFrag : Fragment() {

    private lateinit var binding: FragmentTodoItemBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTodoItemBinding.inflate(inflater, container, false)
        val textView = binding.taskName
        val cardView = binding.taskItemContainer

        binding.taskItemContainer.setOnClickListener{
            val newTaskSheet = NewTaskSheet(todoListFrag = TodoListFrag())
            newTaskSheet.show(parentFragmentManager, "newTaskTag")
        }

        textView.viewTreeObserver.addOnGlobalLayoutListener {
            val textHeight = textView.height
            val layoutParams = cardView.layoutParams
            layoutParams.height = textHeight + 50 // add extra padding
            cardView.layoutParams = layoutParams
        }

        return binding.root
    }
}