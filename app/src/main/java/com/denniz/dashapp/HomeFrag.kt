package com.denniz.dashapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth

class HomeFrag : Fragment() {

    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val startBtn = view.findViewById<Button>(R.id.getStartedButton)
        val logInButton = view.findViewById<Button>(R.id.logInHomeButton)
        auth = FirebaseAuth.getInstance()

        logInButton.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_homeFrag_to_loginFrag)
        }

        startBtn.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeFrag_to_getStartedFrag)
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        if(auth.currentUser != null){
            view?.let { Navigation.findNavController(it).navigate(R.id.dashboardFrag) }
        }
    }


}