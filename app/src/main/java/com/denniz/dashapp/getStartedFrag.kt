package com.denniz.dashapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth


class getStartedFrag : Fragment() {


    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId", "ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_get_started, container, false)
        val signUpBtn = view.findViewById<Button>(R.id.signUpBtn)
        val editTextEmail = view.findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = view.findViewById<EditText>(R.id.editTextPassword)
        val editTextConfirmPassword = view.findViewById<EditText>(R.id.editTextConfirmPassword)
        auth = FirebaseAuth.getInstance()

        signUpBtn.setOnClickListener() {
            val email: String = editTextEmail.text.toString()
            val password: String = editTextPassword.text.toString()
            val confirmPassword: String = editTextConfirmPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()){
                if (password == confirmPassword){
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                        if(it.isSuccessful){
                            Navigation.findNavController(view).navigate(R.id.action_getStartedFrag_to_dashboardFrag)
                        } else {
                            Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        view.findViewById<ImageButton>(R.id.backBtnSignUp).setOnClickListener() {
            Navigation.findNavController(view).navigate(R.id.action_getStartedFrag_to_homeFrag)
        }

        return view
    }
}