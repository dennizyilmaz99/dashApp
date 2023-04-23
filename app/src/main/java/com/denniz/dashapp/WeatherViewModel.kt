package com.denniz.dashapp

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class WeatherViewModel(context: Context) : ViewModel() {

    private val sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

    var cityData: String
        get() = sharedPreferences.getString("cityData", "") ?: ""
        set(value) = sharedPreferences.edit().putString("cityData", value).apply()

    var tempData: String
        get() = sharedPreferences.getString("tempData", "") ?: ""
        set(value) = sharedPreferences.edit().putString("tempData", value).apply()

    var feelsLikeData: String
        get() = sharedPreferences.getString("feelsLikeData", "") ?: ""
        set(value) = sharedPreferences.edit().putString("feelsLikeData", value).apply()

}


