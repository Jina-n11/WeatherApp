package com.cheese.weatherapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.cheese.weatherapp.R
import com.cheese.weatherapp.databinding.ActivityMainBinding
import com.cheese.weatherapp.models.WeatherState
import com.example.mikmok.data.models.WeatherMain
import com.example.mikmok.util.Constants
import com.example.mikmok.util.toCelsius
import com.example.mikmok.util.toPercent
import com.google.gson.Gson
import okhttp3.*
import okhttp3.internal.wait
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val client = OkHttpClient()
    var cityName:String =Constants.CITY
//    var weatherState:String =WeatherState.CLEAR.toString()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onSearchChange()
        onClickSearchButton()
        getWeather(cityName=cityName)

    }

    private fun onClickSearchButton()= binding.buttonSearch.setOnClickListener {
            cityName =  binding.searchCity.text.toString()
            getWeather(cityName=cityName)
        }

    private fun onSearchChange() = binding.searchCity.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            binding.buttonSearch.visibility =android.view.View.VISIBLE
            if(binding.searchCity.text?.isEmpty() == true){
                binding.buttonSearch.visibility =android.view.View.INVISIBLE
            }
            else{
                binding.buttonSearch.visibility =android.view.View.VISIBLE
            }
        }
    })

    private fun getWeather(cityName:String) {
        val request = Request.Builder()
            .url(Constants.API_URL+"${cityName.trim()}")
            .get()
            .addHeader(Constants.ACCEPT, Constants.TYPE)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Toast.makeText(
                    this@MainActivity,
                    "${Constants.ON_FAILURE} ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let { jsonString ->
                    val result = Gson().fromJson(jsonString, WeatherMain::class.java)
                    runOnUiThread {
                        if (result.cod.toString() == Constants.NOT_FOUND) {
                            Toast.makeText(
                                this@MainActivity,
                                result.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else {
                            binding.run {
                                binding.cityName.text = "${result.name},${result.sys.country}"
                                binding.temp.text = "${result.main.temp.toCelsius()}°"
                                binding.description.text = result.weather[Constants.INDEXT_WEATHER].description
                                binding.maxMin.text = "${result.main.tempMax.toCelsius()}° Max - ${result.main.tempMin.toCelsius()}° Min"
                                binding.valueWind.text = "${result.wind.speed} km/h"
                                binding.valueClouds.text = "${result.clouds.all.toPercent()}%"
                                binding.valueHumidity.text = "${result.main.humidity.toPercent()}%"
                                binding.valuePressure.text = "${result.main.pressure.toPercent()}%"
                            }

//                            weatherState = result.weather[Constants.INDEXT_WEATHER].main.uppercase()

                            when (result.weather[Constants.INDEXT_WEATHER].main.uppercase()) {
                                WeatherState.CLOUDS.toString() -> binding.weather.setAnimation(R.raw.cloudy)
                                WeatherState.CLEAR.toString() -> binding.weather.setAnimation(R.raw.sunny)
                                WeatherState.RAIN.toString() -> binding.weather.setAnimation(R.raw.rain)
                            }
                        }

                    }
                }
            }
        })
    }
}