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
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputLayout

class GetToKnowFrag : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_get_to_know, container, false)
        val submitBtn = view.findViewById<Button>(R.id.loginBtn)
        val editTextName = view.findViewById<EditText>(R.id.emailSignup)
        val textInputLayoutName = view.findViewById<TextInputLayout>(R.id.textInputLoginEmail)

        submitBtn.setOnClickListener() {
            val text = "Please type in your name."
            val name: String = editTextName.text.toString()
            if (name.isEmpty()) {
                val colorStateList = ColorStateList(
                    arrayOf(
                        intArrayOf(android.R.attr.state_focused),
                        intArrayOf(-android.R.attr.state_focused)
                    ),
                    intArrayOf(
                        ContextCompat.getColor(requireContext(), R.color.colorError),
                        ContextCompat.getColor(requireContext(), R.color.colorError)
                    ))
            textInputLayoutName.defaultHintTextColor = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.colorError))
            textInputLayoutName.setBoxStrokeColorStateList(colorStateList)
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
            } else {
                sharedViewModel.sharedData = editTextName.text.toString()
            Navigation.findNavController(view).navigate(R.id.action_getToKnowFrag_to_dashboardFrag)
            }
        }



        view.findViewById<ImageButton>(R.id.backBtnLogin).setOnClickListener() {
            Navigation.findNavController(view).navigate(R.id.action_getToKnowFrag_to_getStartedFrag)
        }
        return view
    }
}