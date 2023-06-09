package com.denniz.dashapp

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth


class GetStartedFrag : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels {
        SharedViewModelFactory(requireActivity().application)
    }

    private lateinit var progressBar: ProgressBar

    private lateinit var auth: FirebaseAuth

    private fun showProgressBar() {
        view?.findViewById<ProgressBar>(R.id.progressBarGetStartedFrag)?.apply {
            visibility = View.VISIBLE
        }
    }
    private fun hideProgressBar(){
        view?.findViewById<ProgressBar>(R.id.progressBarGetStartedFrag)?.apply {
            visibility = View.GONE
        }
    }
    @SuppressLint("MissingInflatedId", "ResourceType", "SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_get_started, container, false)
        val signUpBtn = view.findViewById<Button>(R.id.loginBtn)
        val editTextEmail = view.findViewById<EditText>(R.id.emailSignup)
        val editTextPassword = view.findViewById<EditText>(R.id.passwordSignup)
        val editTextConfirmPassword = view.findViewById<EditText>(R.id.editTextConfirmPassword)
        val textInputLayoutEmail = view.findViewById<TextInputLayout>(R.id.textInputLoginEmail)
        val textInputLayoutPassword = view.findViewById<TextInputLayout>(R.id.textInputLoginPassword)
        val textInputLayoutConfirmPassword = view.findViewById<TextInputLayout>(R.id.textInputLayoutConfirmPassword)
        auth = FirebaseAuth.getInstance()
        progressBar = view.findViewById(R.id.progressBarGetStartedFrag)
        progressBar.visibility = View.GONE

        textInputLayoutEmail.editText?.onFocusChangeListener =
            View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    textInputLayoutEmail.setBoxStrokeColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorTurqoise
                        )
                    )
                }
            }
        textInputLayoutPassword.editText?.onFocusChangeListener =
            View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    textInputLayoutPassword.setBoxStrokeColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorTurqoise
                        )
                    )
                }
            }
        textInputLayoutConfirmPassword.editText?.onFocusChangeListener =
            View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    textInputLayoutConfirmPassword.setBoxStrokeColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorTurqoise
                        )
                    )
                }
            }

        signUpBtn.setOnClickListener {
        val email: String = editTextEmail.text.toString()
        val password: String = editTextPassword.text.toString()
        val confirmPassword: String = editTextConfirmPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()){
                showProgressBar()
                Handler().postDelayed({
                    hideProgressBar()
                }, 2000)
                if (password == confirmPassword){
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                        if(it.isSuccessful){
                            sharedViewModel.emailAccountEmail = email
                            sharedViewModel.emailAccountPassword = password
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

        view.findViewById<ImageButton>(R.id.backBtnLogin).setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_getStartedFrag_to_homeFrag)
        }

        return view
    }
}