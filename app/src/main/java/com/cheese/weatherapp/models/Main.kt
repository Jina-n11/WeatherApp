package com.cheese.weatherapp.models

import com.google.gson.annotations.SerializedName

data class Main (
    val temp: Double,

    @SerializedName("feels_like")
    val feelsLike: Double,

    @SerializedName("temp_min")
    val tempMin: Double,

    @SerializedName("temp_max")
    val tempMax: Double,

    val pressure: Double,
    val humidity: Double
)
