package com.denniz.dashapp

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class DashboardFrag : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var progressBar: ProgressBar

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val progressBarView = inflater.inflate(R.layout.progressbar, container, false)
        progressBar = progressBarView.findViewById(R.id.progress_bar)

        val progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Loading Dashboard")
        progressDialog.setMessage("Please wait...")
        progressDialog.isIndeterminate = true
        progressDialog.setCancelable(false)
        progressDialog.show()

        view.visibility = View.GONE
        progressBar.visibility = View.VISIBLE

        Handler(Looper.getMainLooper()).postDelayed({
            progressDialog.dismiss()
            view.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }, 2000) // starta progressbaren efter 1 sekund


        val displayUsername = view.findViewById<TextView>(R.id.displayName)
        val dashboardHeader = view.findViewById<TextView>(R.id.dashboardHeader)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        val user = auth.currentUser
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

        view.findViewById<View>(R.id.weatherViewDashboardFrag).setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_dashboardFrag_to_weatherFrag)
        }

        view.findViewById<ImageButton>(R.id.settingsButton).setOnClickListener{
                Navigation.findNavController(view).navigate(R.id.action_dashboardFrag_to_settingsFrag2)
        }

         return view
}

    override fun onDestroyView() {
        super.onDestroyView()
        Handler().removeCallbacksAndMessages(null)
    }

}