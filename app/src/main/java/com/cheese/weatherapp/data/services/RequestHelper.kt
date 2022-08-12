package com.cheese.weatherapp.data.services

import com.example.mikmok.util.Constants
import com.example.mikmok.util.Params
import okhttp3.HttpUrl
import okhttp3.Request

object RequestHelper {
    fun makeWeatherRequest(cityName: String): Request {
        val url= HttpUrl.Builder()
            .scheme(Constants.SCHEMA)
            .host(Constants.HOST)
            .addPathSegment(Constants.PATH)
            .addQueryParameter(Params.UNITS, Params.Values.units)
            .addQueryParameter(Params.APP_ID, Params.Values.APP_ID)
            .addQueryParameter(Params.CITY,cityName)
            .build()
        return Request.Builder()
            .url(url)
            .get()
            .addHeader(Constants.ACCEPT, Constants.TYPE)
            .build()
    }
}