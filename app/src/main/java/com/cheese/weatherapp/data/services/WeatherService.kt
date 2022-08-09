package com.cheese.weatherapp.data.services

import com.cheese.weatherapp.data.State
import com.cheese.weatherapp.data.models.Weather
import com.google.gson.Gson
import okhttp3.OkHttpClient

interface BaseWeatherService {
    fun getWeatherByCityName(cityName: String): State<Weather>

}

class WeatherService : BaseWeatherService {
    private val client = OkHttpClient()
    override fun getWeatherByCityName(cityName: String): State<Weather> {
        val response = client.newCall(RequestHelper.makeWeatherRequest(cityName)).execute()
        return if (response.isSuccessful) {
            Gson().fromJson(response.body?.string(), Weather::class.java).run {
                State.Success(this)
            }
        } else {
            State.Fail(response.message)
        }
    }
}
