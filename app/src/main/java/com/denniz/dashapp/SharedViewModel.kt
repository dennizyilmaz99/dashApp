package com.denniz.dashapp

import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {
    var sharedData: String = ""

    fun greetingText(): String {
        return "Hello, $sharedData."
    }
}