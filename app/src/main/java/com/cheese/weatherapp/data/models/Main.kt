package com.cheese.weatherapp.data.models

import com.google.gson.annotations.SerializedName

data class Main (
    @SerializedName("temp")
    val temp: Double,
    @SerializedName("temp_min")
    val tempMin: Double,
    @SerializedName("temp_max")
    val tempMax: Double,
    @SerializedName("pressure")
    val pressure: Double,
    @SerializedName("humidity")
    val humidity: Double
)
