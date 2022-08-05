package com.cheese.weatherapp.ui
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
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
import java.io.IOException

class MainActivity : BaseActivity<ActivityMainBinding>(){
    private val client = OkHttpClient()
    var cityName:String =Constants.CITY
    override val LOG_TAG: String =Constants.MAIN_ACTIVITY
    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding = ActivityMainBinding::inflate

    override fun setUp() {
      getWeather(cityName=cityName)
    }

    override fun addCallbacks() {
        onSearchChange()
        onClickSearchButton()
    }

    private fun onClickSearchButton()= binding.buttonSearch.setOnClickListener {
            cityName =  binding.searchCity.text.toString()
            getWeather(cityName=cityName)
    }

    private fun onSearchChange() = binding.searchCity.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            visibleButton()
        }
    })

    private fun visibleButton(){
        binding.buttonSearch.visibility =android.view.View.VISIBLE
        if(binding.searchCity.text.isEmpty()){
            binding.buttonSearch.visibility =android.view.View.INVISIBLE
        }
        else{
            binding.buttonSearch.visibility =android.view.View.VISIBLE
        }
    }

    private fun getWeather(cityName:String) {
        val request = Request.Builder()
            .url(Constants.API_URL+"${cityName.trim()}")
            .get()
            .addHeader(Constants.ACCEPT, Constants.TYPE)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
               showToast("${Constants.ON_FAILURE} ${e.message}")
            }
            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let { jsonString ->
                    val result = Gson().fromJson(jsonString, WeatherMain::class.java)

                    runOnUiThread {
                        if (result.cod.toString() == Constants.NOT_FOUND) {
                         showToast(result.message)
                        }
                        else {
                            attributBinding(result)
                            weatherState(result.weather[Constants.INDEXT_WEATHER].main.uppercase())
                        }

                    }
                }
            }
        })
    }

    private fun attributBinding(result:WeatherMain){
        binding.apply {
            cityName.text = "${result.name},${result.sys.country}"
            temp.text = "${result.main.temp.toCelsius()}°"
            description.text = result.weather[Constants.INDEXT_WEATHER].description
            maxMin.text = "${result.main.tempMax.toCelsius()}° Max - ${result.main.tempMin.toCelsius()}° Min"
            valueWind.text = "${result.wind.speed} km/h"
            valueClouds.text = "${result.clouds.all.toPercent()}%"
            valueHumidity.text = "${result.main.humidity.toPercent()}%"
            valuePressure.text = "${result.main.pressure.toPercent()}%"
        }
    }

    private fun weatherState(weatherState:String){
        when (weatherState) {
            WeatherState.CLOUDS.toString() -> {
                binding.weather.setAnimation(R.raw.cloudy)
                binding.weather.playAnimation()
            }
            WeatherState.CLEAR.toString() -> {
                binding.weather.setAnimation(R.raw.sunny)
                binding.weather.playAnimation()
            }
            WeatherState.RAIN.toString() -> {
                binding.weather.setAnimation(R.raw.rain)
                binding.weather.playAnimation()
            }
        }
    }

}
