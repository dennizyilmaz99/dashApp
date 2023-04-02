package com.denniz.dashapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation

class HomeFrag : Fragment() {


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val startBtn = view.findViewById<Button>(R.id.getStartedButton)

        startBtn.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeFrag_to_getStartedFrag)
        }











        return view
    }



}