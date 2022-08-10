package com.cheese.weatherapp.ui
import android.view.LayoutInflater
import androidx.core.widget.doOnTextChanged
import com.cheese.weatherapp.R
import com.cheese.weatherapp.databinding.ActivityMainBinding
import com.cheese.weatherapp.data.models.WeatherState
import com.cheese.weatherapp.data.request.ApiClient
import com.example.mikmok.data.models.WeatherMain
import com.example.mikmok.util.Constants
import com.example.mikmok.util.toCelsius
import com.example.mikmok.util.toPercent
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity<ActivityMainBinding>(){
    var cityName:String =Constants.CITY
    val gson =Gson()
    private val apiClient by lazy { ApiClient() }
    override val LOG_TAG: String =Constants.MAIN_ACTIVITY
    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding = ActivityMainBinding::inflate

    override fun setUp() {
      getWeather(cityName=cityName)
    }
    override fun addCallbacks() {
         onSearchChange()
    }


    private fun onSearchChange(){
        val observableSearch =Observable.create<String>{ emitter->
            binding.searchCity.doOnTextChanged { text, start, before, count ->
                if (count !=Constants.INDEXT_COUNT_EMPTY) {
                    emitter.onNext(text.toString())
                }
            }
        }.debounce(Constants.TIME.toLong(),TimeUnit.SECONDS)
        observableSearch.subscribe(
            {cityName->getWeather(cityName=cityName)},
            {notFound-> showToast(message = Constants.CITY_NOT_FOUND)
            }
        )
    }


    private fun getWeather(cityName:String) {
        apiClient.makeApiRequest(cityName=cityName.trim()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
               showToast("${Constants.ON_FAILURE} ${e.message}")
            }
            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let { jsonString ->
                    val result = gson.fromJson(jsonString, WeatherMain::class.java)

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
