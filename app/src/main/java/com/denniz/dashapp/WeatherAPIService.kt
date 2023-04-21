package com.denniz.dashapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("current")
    fun getWeatherData(
        @Query("city") city: String,
        @Query("key") key: String
    ): Call<WeatherData>
}

