package com.denniz.dashapp

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class SharedViewModel(private val context: Context) : ViewModel() {

    private val sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

    var sharedData: String
        get() = sharedPreferences.getString("sharedData", "") ?: ""
        set(value) = sharedPreferences.edit().putString("sharedData", value).apply()

    var emailAccount: String
        get() = sharedPreferences.getString("emailAccount", "") ?: ""
        set(value) = sharedPreferences.edit().putString("emailAccount", value).apply()

    fun greetingText(): String {
        return "Hello, $sharedData."
    }

    fun dashboardHeader(): String {
        return "$sharedData's Dashboard"
    }

    fun displayEmail(): String {
        return "Email: $emailAccount"
    }

}
