package com.cheese.weatherapp.data.services

import com.cheese.weatherapp.data.State
import com.cheese.weatherapp.data.models.Weather
import com.example.mikmok.data.models.WeatherMain
import com.google.gson.Gson
import okhttp3.OkHttpClient

interface BaseWeatherService {
    fun getWeatherByCityName(cityName: String): State<WeatherMain>

}

class WeatherService : BaseWeatherService {
    private val client = OkHttpClient()
    override fun getWeatherByCityName(cityName: String): State<WeatherMain> {
        val response = client.newCall(RequestHelper.makeWeatherRequest(cityName)).execute()
        return if (response.isSuccessful) {
            Gson().fromJson(response.body?.string(), WeatherMain::class.java).run {
                State.Success(this)
            }
        } else {
            State.Fail(response.message)
        }
    }
}
