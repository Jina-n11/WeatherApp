package com.cheese.weatherapp.models

import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("1h")
    val one_h:Double?
)
