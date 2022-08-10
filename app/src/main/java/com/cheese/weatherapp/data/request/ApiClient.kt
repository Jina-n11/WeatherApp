package com.cheese.weatherapp.data.request

import com.example.mikmok.util.Constants
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request

class ApiClient {

    private val client = OkHttpClient()
    fun makeApiRequest(cityName :String): Call {
        val request = Request.Builder()
            .url(Constants.API_URL+"${cityName.trim()}")
            .get()
            .addHeader(Constants.ACCEPT, Constants.TYPE)
            .build()

        return client.newCall(request)
    }

}