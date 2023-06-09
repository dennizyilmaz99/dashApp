package com.denniz.dashapp

import android.content.Context
import androidx.lifecycle.ViewModel

class SharedViewModel(context: Context) : ViewModel() {

    private val sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

    var sharedData: String
        get() = sharedPreferences.getString("sharedData", "") ?: ""
        set(value) = sharedPreferences.edit().putString("sharedData", value).apply()

    var emailAccountEmail: String
        get() = sharedPreferences.getString("emailAccount", "") ?: ""
        set(value) = sharedPreferences.edit().putString("emailAccount", value).apply()

    var emailAccountPassword: String
        get() = sharedPreferences.getString("emailAccountPassword", "") ?: ""
        set(value) = sharedPreferences.edit().putString("emailAccountPassword", value).apply()

}
