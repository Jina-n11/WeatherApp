package com.cheese.weatherapp.data.services

import com.example.mikmok.util.Constants
import okhttp3.Request

object RequestHelper {
    fun makeWeatherRequest(cityName: String): Request {
        return Request.Builder()
            .url(Constants.API_URL + "${cityName.trim()}")
            .get()
            .addHeader(Constants.ACCEPT, Constants.TYPE)
            .build()
    }
}