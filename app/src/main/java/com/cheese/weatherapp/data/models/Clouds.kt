package com.cheese.weatherapp.data.models

import com.google.gson.annotations.SerializedName

data class Clouds (
    @SerializedName("all")
    val all: Double
)
