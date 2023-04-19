package com.denniz.dashapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("weather")
    fun getWeatherData(
        @Query("city_name") city: String,
        @Query("f2e229da776747148151cb345a6d27c3") apiKey: String
    ): Call<WeatherData>
}

