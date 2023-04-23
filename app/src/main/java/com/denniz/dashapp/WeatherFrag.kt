package com.denniz.dashapp

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import com.denniz.dashapp.databinding.FragmentWeatherBinding
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherFrag : Fragment() {

    private lateinit var binding: FragmentWeatherBinding
    private val apiKey = "f2e229da776747148151cb345a6d27c3"
    private lateinit var progressBar: ProgressBar
    private fun showProgressBar() {
        binding.progressBarWeatherFrag.apply {
            visibility = View.VISIBLE
        }
    }
    private fun hideProgressBar(){
        binding.progressBarWeatherFrag.apply {
            visibility = View.GONE
        }
    }

    private val weatherViewModel: WeatherViewModel by activityViewModels {
        WeatherViewModelFactory(requireActivity().application)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeatherBinding.inflate(inflater, container, false)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.weatherbit.io/v2.0/")
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()

        val weatherApiService = retrofit.create(WeatherApiService::class.java)

        val temperatureTextView = binding.tempNumberWeatherFrag
        val displayCityName = binding.tempCityWeatherFrag
        val searchCityInputLayout = binding.searchCityInputLayout
        val feelsLikeTemp = binding.feelslikeTemp
        progressBar = binding.progressBarWeatherFrag
        progressBar.visibility = View.GONE


        binding.searchButton.setOnClickListener {
            println("KNAPPEN TRYCKT")
            val cityEditText = binding.searchCityEditText.text.toString()
            if (cityEditText.isNotEmpty()) {
                showProgressBar()
                Handler().postDelayed({
                    hideProgressBar()
                }, 650)
                val call = weatherApiService.getWeatherData(cityEditText, apiKey)
                call.enqueue(object : Callback<WeatherData> {
                    @SuppressLint("SetTextI18n")
                    override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                        if (response.isSuccessful) {
                            val weatherResponse = response.body()
                            val weatherData = weatherResponse?.data?.firstOrNull()
                            val temperature = weatherData?.app_temp?.toFloat()


                            weatherViewModel.cityData = cityEditText
                            weatherViewModel.tempData = "${temperature?.toInt()}°"
                            weatherViewModel.feelsLikeData = "Feels like: ${temperature?.toInt()}°"

                            displayCityName.text = cityEditText
                            temperatureTextView.text = "${temperature?.toInt()}°"
                            feelsLikeTemp.text = "Feels like: ${temperature?.toInt()}°"

                        } else {
                            println("Data är null tyvärr.")
                        }
                    }
                    override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                        Log.d("WeatherFrag", "Kunde inte hämta väderdata: ${t.localizedMessage}")
                    }
                })
            }
            if (cityEditText.isEmpty()){
                val colorStateList = ColorStateList(
                    arrayOf(
                        intArrayOf(android.R.attr.state_focused),
                        intArrayOf(-android.R.attr.state_focused)
                    ),
                    intArrayOf(
                        ContextCompat.getColor(requireContext(), R.color.colorError),
                        ContextCompat.getColor(requireContext(), R.color.colorError)
                    ))
                searchCityInputLayout.defaultHintTextColor = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.colorError))
                searchCityInputLayout.setBoxStrokeColorStateList(colorStateList)

                Toast.makeText(context, "Please type in city.", Toast.LENGTH_SHORT).show()
            }
        }

        displayCityName.text = weatherViewModel.cityData
        temperatureTextView.text = weatherViewModel.tempData
        feelsLikeTemp.text = weatherViewModel.feelsLikeData

        binding.backBtnWeather.setOnClickListener {
            val bundle = bundleOf("fromFragment" to "weatherFrag")
            Navigation.findNavController(binding.root).navigate(R.id.action_weatherFrag_to_dashboardFrag, bundle)

        }
        return binding.root

    }
}
