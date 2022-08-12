package com.cheese.weatherapp.data.models

import com.google.gson.annotations.SerializedName

data class WeatherMain(
    @SerializedName("wind")
    val wind: Wind,
    @SerializedName("rain")
    val rain :Rain,
    @SerializedName("clouds")
    val clouds: Clouds,
    @SerializedName("name")
    val name: String,
    @SerializedName("weather")
    val weather: List<Weather>,
    @SerializedName("base")
    val base: String,
    @SerializedName("main")
    val main: Main,
    @SerializedName("sys")
    val sys: Sys,
    @SerializedName("cod")
    val cod: Int,
    @SerializedName("message")
    val message: String,


)
