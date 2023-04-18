package com.denniz.dashapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.denniz.dashapp.databinding.FragmentNewTaskSheetBinding
import com.denniz.dashapp.databinding.FragmentSettingsBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class SettingsFrag() : BottomSheetDialogFragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the NavController on the view
        Navigation.setViewNavController(view, findNavController())

        // Rest of your code
        val logoutButton = view.findViewById<Button>(R.id.logoutButton)
        val displayEmail = view.findViewById<TextView>(R.id.displayEmail)
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val docRef = user!!.email?.let {
            FirebaseFirestore.getInstance().collection("users").document(
                it
            )
        }
        docRef!!.get()
            .addOnSuccessListener { document ->
                if (document.exists()){
                    val field = document.getString("Email")
                    displayEmail.text = "Email: $field"
                }
            }


        logoutButton.setOnClickListener{
            auth.signOut()
            Navigation.findNavController(view).navigate(R.id.action_settingsFrag2_to_homeFrag)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }
}

