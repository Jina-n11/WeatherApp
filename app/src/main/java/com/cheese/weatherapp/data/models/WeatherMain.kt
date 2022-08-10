package com.cheese.weatherapp.data.models

data class WeatherMain(
    val wind: Wind,
    val rain :Rain,
    val clouds: Clouds,
    val name: String,
    val weather: List<Weather>,
    val base: String,
    val main: Main,
    val sys: Sys,
    val cod: Int,
    val message: String,

)
