package com.denniz.dashapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.Navigation
import com.denniz.dashapp.databinding.FragmentNewTaskSheetBinding
import com.denniz.dashapp.databinding.FragmentWeatherBinding
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class WeatherFrag : Fragment() {

    private lateinit var binding: FragmentWeatherBinding
    private lateinit var weatherApiService: WeatherApiService
    private val apiKey = "f2e229da776747148151cb345a6d27c3"
    private val gson = Gson()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWeatherBinding.inflate(inflater, container, false)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.weatherbit.io/v2.0/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        weatherApiService = retrofit.create(WeatherApiService::class.java)

       
        binding.searchButton.setOnClickListener {
            val city = binding.searchCityEditText.text.toString()
            if (city.isNotEmpty()) {
                val url = "https://api.weatherbit.io/v2.0/current?city=$city&key=$apiKey"
                val call = weatherApiService.getWeatherData(url, apiKey)
                call.enqueue(object : Callback<WeatherData> {
                    override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                        if (response.isSuccessful) {
                            val weatherResponse = response.body()
                            if (weatherResponse != null && weatherResponse.data.isNotEmpty()){
                                val weatherData = weatherResponse.data[0]
                                val temperatureTextView = binding.temperatureTextView
                                temperatureTextView.text = "${weatherData.appTemp}°C"
                            }

                        }
                    }
                    override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                        // Hantera fel här
                    }
                })
            } else {
                // Visa felmeddelande om användaren inte har angett någon stad
            }
        }



        binding.backBtnWeather.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_weatherFrag_to_dashboardFrag)
        }
        return binding.root

    }
}
