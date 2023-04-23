package com.denniz.dashapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class GetToKnowFrag : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar: ProgressBar
    private fun showProgressBar() {
        view?.findViewById<ProgressBar>(R.id.progressBarGetToKnowFrag)?.apply {
            visibility = View.VISIBLE
        }
    }
    private fun hideProgressBar(){
        view?.findViewById<ProgressBar>(R.id.progressBarGetToKnowFrag)?.apply {
            visibility = View.GONE
        }
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_get_to_know, container, false)
        val submitBtn = view.findViewById<Button>(R.id.loginBtn)
        val editTextName = view.findViewById<EditText>(R.id.get_to_know_Name)
        val textInputLayoutName = view.findViewById<TextInputLayout>(R.id.textInputLoginEmail)
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        progressBar = view.findViewById(R.id.progressBarGetToKnowFrag)
        progressBar.visibility = View.GONE

        submitBtn.setOnClickListener {
            val text = "Please type in your name."
            val name: String = editTextName.text.toString()
            if (name.isNotEmpty()){
                showProgressBar()
                Handler().postDelayed({
                    hideProgressBar()
                }, 2000)
            }
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
                val user: MutableMap<String, Any> = HashMap()
                user["Email"] = sharedViewModel.emailAccountEmail
                user["Name"] = name
                user["User-ID"] = currentUser!!.uid
                db.collection("users")
                    .document(sharedViewModel.emailAccountEmail)
                    .set(user)
                    .addOnFailureListener { e ->
                        Log.w(
                            ContentValues.TAG,
                            "Error adding document",
                            e
                        )
                    }
                sharedViewModel.sharedData = editTextName.text.toString()
                Navigation.findNavController(view).navigate(R.id.action_getToKnowFrag_to_dashboardFrag)
            }
        }
        view.findViewById<ImageButton>(R.id.backBtnLogin).setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_getToKnowFrag_to_getStartedFrag)
        }
        return view

    }
}