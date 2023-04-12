package com.denniz.dashapp

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation


class DashboardFrag : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val displayUsername = view.findViewById<TextView>(R.id.displayName)
        val dashboardHeader = view.findViewById<TextView>(R.id.dashboardHeader)

        view.findViewById<View>(R.id.taskViewButton).setOnClickListener() {
            Navigation.findNavController(view).navigate(R.id.action_dashboardFrag_to_todoListFrag)
        }

        view.findViewById<ImageButton>(R.id.settingsButton).setOnClickListener{
                Navigation.findNavController(view).navigate(R.id.action_dashboardFrag_to_settingsFrag2)
        }

        sharedViewModel.dashboardHeader().let{
            dashboardHeader.text = it
        }

        sharedViewModel.greetingText().let{
            displayUsername.text = it
        }

         return view
}}