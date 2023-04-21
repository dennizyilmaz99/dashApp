package com.denniz.dashapp

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
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
        binding.searchButton.setOnClickListener {
            println("KNAPPEN TRYCKT")
            val cityEditText = binding.searchCityEditText.text.toString()

            if (cityEditText.isNotEmpty()) {
                val call = weatherApiService.getWeatherData(cityEditText, apiKey)
                call.enqueue(object : Callback<WeatherData> {
                    @SuppressLint("SetTextI18n")
                    override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                        if (response.isSuccessful) {
                            val weatherResponse = response.body()
                            val weatherData = weatherResponse?.data?.firstOrNull()
                            val temperature = weatherData?.app_temp?.toFloat()
                            val iconCode = weatherData?.weather?.icon


                            displayCityName.text = cityEditText
                            temperatureTextView.text = "${temperature?.toInt()}°"
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

        binding.backBtnWeather.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_weatherFrag_to_dashboardFrag)
        }
        return binding.root

    }
}
