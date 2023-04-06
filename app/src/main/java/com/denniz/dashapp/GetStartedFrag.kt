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
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth


class GetStartedFrag : Fragment() {


    private lateinit var auth: FirebaseAuth
    @SuppressLint("MissingInflatedId", "ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_get_started, container, false)
        val signUpBtn = view.findViewById<Button>(R.id.submitBtn)
        val editTextEmail = view.findViewById<EditText>(R.id.editTextName)
        val editTextPassword = view.findViewById<EditText>(R.id.editTextPassword)
        val editTextConfirmPassword = view.findViewById<EditText>(R.id.editTextConfirmPassword)
        val textInputLayoutEmail = view.findViewById<TextInputLayout>(R.id.textInputLayoutName)
        val textInputLayoutPassword = view.findViewById<TextInputLayout>(R.id.textInputLayoutPassword)
        val textInputLayoutConfirmPassword = view.findViewById<TextInputLayout>(R.id.textInputLayoutConfirmPassword)
        auth = FirebaseAuth.getInstance()

        signUpBtn.setOnClickListener() {
            val email: String = editTextEmail.text.toString()
            val password: String = editTextPassword.text.toString()
            val confirmPassword: String = editTextConfirmPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()){
                if (password == confirmPassword){
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                        if(it.isSuccessful){
                            Navigation.findNavController(view).navigate(R.id.action_getStartedFrag_to_getToKnowFrag)
                        } else {
                            Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            if (email.isEmpty() && password.isEmpty() && confirmPassword.isEmpty()){
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
                textInputLayoutConfirmPassword.defaultHintTextColor = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.colorError))
                textInputLayoutConfirmPassword.setBoxStrokeColorStateList(colorStateList)
            }
        }

        view.findViewById<ImageButton>(R.id.backBtnSignUp).setOnClickListener() {
            Navigation.findNavController(view).navigate(R.id.action_getStartedFrag_to_homeFrag)
        }

        view.findViewById<Button>(R.id.button2).setOnClickListener() {
            Navigation.findNavController(view).navigate(R.id.action_getStartedFrag_to_getToKnowFrag)
        }

        return view
    }
}