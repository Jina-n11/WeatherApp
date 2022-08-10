package com.cheese.weatherapp.data.models

import com.google.gson.annotations.SerializedName

data class Main (
    val temp: Double,

    @SerializedName("temp_min")
    val tempMin: Double,

    @SerializedName("temp_max")
    val tempMax: Double,

    val pressure: Double,
    val humidity: Double
)
