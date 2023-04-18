package com.denniz.dashapp

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentValues.TAG
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.ViewModelFactoryDsl
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.handleCoroutineException


class DashboardFrag : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels {
        SharedViewModelFactory(requireActivity().application)
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val displayUsername = view.findViewById<TextView>(R.id.displayName)
        val dashboardHeader = view.findViewById<TextView>(R.id.dashboardHeader)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        val user = auth.currentUser
        val userID = auth.currentUser!!.uid
        val docRef = user!!.email?.let {
            FirebaseFirestore.getInstance().collection("users").document(
                it
            )
        }

        docRef!!.get()
            .addOnSuccessListener { document ->
                if (document.exists()){
                    val field = document.getString("Name")
                    dashboardHeader.text = "$field's Dashboard"
                    displayUsername.text = "Hello, $field."
                }
            }

        view.findViewById<View>(R.id.taskViewButton).setOnClickListener() {
            Navigation.findNavController(view).navigate(R.id.action_dashboardFrag_to_todoListFrag)
        }

        view.findViewById<View>(R.id.weatherView).setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_dashboardFrag_to_weatherFrag)
        }

        view.findViewById<ImageButton>(R.id.settingsButton).setOnClickListener{
                Navigation.findNavController(view).navigate(R.id.action_dashboardFrag_to_settingsFrag2)
        }


         return view
}}