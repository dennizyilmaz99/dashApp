package com.denniz.dashapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.Navigation

class TodoListFrag : Fragment() {


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_todo_list, container, false)

        view.findViewById<ImageButton>(R.id.backBtnTodoList).setOnClickListener(){
            Navigation.findNavController(view).navigate(R.id.action_todoListFrag_to_dashboardFrag2)
        }

        return view
    }
}