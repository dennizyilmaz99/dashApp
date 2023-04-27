package com.denniz.dashapp

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
class WeatherViewModel(context: Context) : ViewModel() {

    private val sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

    var cityData: String?
        get() = sharedPreferences.getString("cityData", null)
        set(value) {
            sharedPreferences.edit().putString("cityData", value).apply()
            Log.d("WeatherViewModel", "cityData saved: $value")
        }

    var tempData: String?
        get() = sharedPreferences.getString("tempData", null)
        set(value) {
            sharedPreferences.edit().putString("tempData", value).apply()
            Log.d("WeatherViewModel", "tempData saved: $value")
        }

    var feelsLikeData: String?
        get() = sharedPreferences.getString("feelsLikeData", null)
        set(value) {
            sharedPreferences.edit().putString("feelsLikeData", value).apply()
            Log.d("WeatherViewModel", "feelsLikeData saved: $value")
        }

}



