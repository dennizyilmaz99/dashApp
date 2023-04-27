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
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class DashboardFrag : Fragment() {

    private lateinit var displayCityDash: TextView
    private lateinit var displayTempDash: TextView
    private lateinit var feelsLike: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var progressDialog: ProgressDialog
    private var shouldShowProgressDialog = false
    private var isFragmentCreated = false
    private var fromFragment: String? = null
    private val weatherViewModel: WeatherViewModel by viewModels() {
        WeatherViewModelFactory(requireActivity().application)
    }
    private fun showProgressDialog(fromFragment: String) {
        shouldShowProgressDialog = true
        this.fromFragment = fromFragment
    }
    private fun hideProgressDialog() {
        shouldShowProgressDialog = false
        fromFragment = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        displayCityDash = view.findViewById(R.id.tempCityDashboardFrag)
        displayTempDash = view.findViewById(R.id.tempNumberDashboardFrag)
        feelsLike = view.findViewById(R.id.feelsLikeTempDash)

        displayCityDash.text = weatherViewModel.cityData
        displayTempDash.text = weatherViewModel.tempData
        feelsLike.text = weatherViewModel.feelsLikeData
    }

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Loading Dashboard")
        progressDialog.setMessage("Please wait...")
        progressDialog.isIndeterminate = true
        progressDialog.setCancelable(false)
        fromFragment = arguments?.getString("fromFragment")
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val displayUsername = view.findViewById<TextView>(R.id.displayName)
        val dashboardHeader = view.findViewById<TextView>(R.id.dashboardHeader)
        displayCityDash = view.findViewById(R.id.tempCityDashboardFrag)
        displayTempDash = view.findViewById(R.id.tempNumberDashboardFrag)
        feelsLike = view.findViewById(R.id.feelsLikeTempDash)
        val user = auth.currentUser
        val docRef = user!!.email?.let {
            FirebaseFirestore.getInstance().collection("users").document(
                it
            )
        }

        if (savedInstanceState != null) {
            displayCityDash.text = savedInstanceState.getString("cityData")
            displayTempDash.text = savedInstanceState.getString("tempData")
            feelsLike.text = savedInstanceState.getString("feelsLikeData")
        } else {
            displayCityDash.text = weatherViewModel.cityData
            displayTempDash.text = weatherViewModel.tempData
            feelsLike.text = weatherViewModel.feelsLikeData
        }

        if (fromFragment == null || (fromFragment != "weatherFrag" && fromFragment != "todoListFrag")) {
            progressDialog.show()
            view.visibility = View.GONE
        }

        Handler(Looper.getMainLooper()).postDelayed({
            progressDialog.dismiss()
            view?.visibility = View.VISIBLE
        }, 2000)

        docRef!!.get()
            .addOnSuccessListener { document ->
                if (document.exists()){
                    val field = document.getString("Name")
                    dashboardHeader.text = "$field's Dashboard"
                    displayUsername.text = "Hello, $field."
                }
            }

        view.findViewById<View>(R.id.taskViewButton).setOnClickListener {
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

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("cityData", weatherViewModel.cityData)
        outState.putString("tempData", weatherViewModel.tempData)
        outState.putString("feelsLikeData", weatherViewModel.feelsLikeData)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Handler().removeCallbacksAndMessages(null)
        isFragmentCreated = false
    }

    override fun onResume() {
        super.onResume()
        if (fromFragment == "weatherFrag" || fromFragment == "todoListFrag"){
            showProgressDialog(fromFragment!!)
            Handler(Looper.getMainLooper()).postDelayed({
                progressDialog.dismiss()
                view?.visibility = View.VISIBLE
            }, 2000)
        } else {
            hideProgressDialog()
        }
    }
}

