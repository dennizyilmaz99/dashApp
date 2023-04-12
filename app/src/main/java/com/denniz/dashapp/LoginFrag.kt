package com.denniz.dashapp

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.denniz.dashapp.databinding.FragmentLoginBinding
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth


class LoginFrag : Fragment() {

    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        val textInputLayoutEmail = view.findViewById<TextInputLayout>(R.id.textInputLoginEmail)
        val textInputLayoutPassword = view.findViewById<TextInputLayout>(R.id.textInputLoginPassword)
        val backBtnLogin = view.findViewById<ImageButton>(R.id.backBtnLogin)
        val logInBtn = view.findViewById<Button>(R.id.loginBtn)
        val editTextEmail = view.findViewById<EditText>(R.id.emailLogin)
        val editTextPassword = view.findViewById<EditText>(R.id.passwordLogin)
        auth = FirebaseAuth.getInstance()

        backBtnLogin.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_loginFrag_to_homeFrag)
        }

        logInBtn.setOnClickListener() {
            val email: String = editTextEmail.text.toString()
            val password: String = editTextPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()){
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
                        if(it.isSuccessful){
                            Navigation.findNavController(view).navigate(R.id.action_loginFrag_to_dashboardFrag)
                        } else {
                            Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            if (email.isEmpty() && password.isEmpty()){
                val text = "No credentials. Please try again."
                val colorStateList = ColorStateList(
                    arrayOf(
                        intArrayOf(android.R.attr.state_focused),
                        intArrayOf(-android.R.attr.state_focused)
                    ),
                    intArrayOf(
                        ContextCompat.getColor(requireContext(), R.color.colorError),
                        ContextCompat.getColor(requireContext(), R.color.colorError)
                    ))
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
                textInputLayoutEmail.defaultHintTextColor = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.colorError))
                textInputLayoutEmail.setBoxStrokeColorStateList(colorStateList)
                textInputLayoutPassword.defaultHintTextColor = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.colorError))
                textInputLayoutPassword.setBoxStrokeColorStateList(colorStateList)
            }
        }
        return view
    }
}