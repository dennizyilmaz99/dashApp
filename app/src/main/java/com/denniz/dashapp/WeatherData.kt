package com.denniz.dashapp

import com.google.gson.annotations.SerializedName

data class WeatherData(
    @SerializedName("app_temp") val appTemp: Int,
    @SerializedName("aqi") val airQualityIndex: Int,
    @SerializedName("city_name") val cityName: String,
    @SerializedName("clouds") val clouds: Int,
    @SerializedName("country_code") val countryCode: String,
    @SerializedName("datetime") val dateTime: String,
    @SerializedName("dewpt") val dewPoint: Double,
)
data class WeatherResponse(
    val count: Int,
    val data: List<WeatherData>
)
