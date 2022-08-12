package com.cheese.weatherapp.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.widget.doOnTextChanged
import com.cheese.weatherapp.R
import com.cheese.weatherapp.data.State
import com.cheese.weatherapp.data.models.WeatherMain
import com.cheese.weatherapp.data.repository.WeatherRepositoryImp
import com.cheese.weatherapp.data.services.WeatherService
import com.cheese.weatherapp.databinding.ActivityMainBinding
import com.cheese.weatherapp.util.getWeatherStateAnimation
import com.cheese.weatherapp.util.toPercent
import com.example.mikmok.util.Constants
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity<ActivityMainBinding>() {
    private var cityName: String = Constants.CITY
    override val LOG_TAG: String = Constants.MAIN_ACTIVITY
    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding =
        ActivityMainBinding::inflate

    override fun setUp() {
        getWeather(cityName = cityName)
    }

    override fun addCallbacks() {
        onSearchChange()
    }

    private fun onSearchChange() {
        val observableSearch = Observable.create<String> { emitter ->
            binding.searchCity.doOnTextChanged { text, _, _, count ->
                if (count != Constants.INDEX_COUNT_EMPTY) {
                    emitter.onNext(text.toString())
                }
            }
        }.debounce(Constants.TIME.toLong(), TimeUnit.SECONDS)
        observableSearch.subscribe(
            { cityName -> getWeather(cityName = cityName) },
            {
                showToast(message = Constants.CITY_NOT_FOUND)
            }
        )
    }


    private fun getWeather(cityName: String) {
        val weatherService = WeatherService()
        val weatherRepositoryImp = WeatherRepositoryImp(weatherService)
        val observer = weatherRepositoryImp.getWeatherByCityName(cityName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        observer.subscribe(::onWeather, ::onError)
    }

    private fun onError(throwable: Throwable) {
        Log.v(LOG_TAG, throwable.message.toString())
    }

    private fun onWeather(state: State<WeatherMain>) {
        when (state) {
            is State.Fail -> onFail()
            State.Loading -> onLoading()
            is State.Success -> onSuccess(state.data)
        }
    }

    private fun onFail() {
        changeProgressLoadingVisibility(View.INVISIBLE)
        changeDataContainerVisibility(View.INVISIBLE)
        changeErrorDataVisibility(View.VISIBLE)

    }

    private fun onSuccess(weatherMain: WeatherMain) {
        bindView(weatherMain)
        hideProgressBarAndShowData()


    }

    private fun onLoading() {
        showProgressBarAndHideData()
    }

    private fun changeDataContainerVisibility(visibility: Int) {
        binding.dataContainer.visibility = visibility
    }

    private fun changeProgressLoadingVisibility(visibility: Int) {
        binding.progressLoading.visibility = visibility
    }

    private fun changeErrorDataVisibility(visibility: Int) {
        binding.apply {
            errorLottie.visibility = visibility
            citeNotFoundText.visibility = visibility
        }
    }


    private fun showProgressBarAndHideData() {
        changeProgressLoadingVisibility(View.VISIBLE)
        changeDataContainerVisibility(View.INVISIBLE)
    }

    private fun hideProgressBarAndShowData() {
        changeProgressLoadingVisibility(View.INVISIBLE)
        changeDataContainerVisibility(View.VISIBLE)
        changeErrorDataVisibility(View.INVISIBLE)
    }

    private fun bindView(result: WeatherMain) {
        binding.apply {
            cityName.text = "${result.name},${result.sys.country}"
            temp.text = "${result.main.temp}°"
            description.text = result.weather[Constants.INDEX_WEATHER].description
            val maxText = getString(R.string.max_text)
            val minText = getString(R.string.min_text)
            val percentage = getString(R.string.percentage)
            maxMin.text =
                "${result.main.tempMax} $maxText - ${result.main.tempMin} $minText"
            valueWind.text = "${result.wind.speed} km/h"
            valueClouds.text = "${result.clouds.all.toPercent()}$percentage"
            valueHumidity.text = "${result.main.humidity.toPercent()}$percentage"
            valuePressure.text = "${result.main.pressure.toPercent()}$percentage"
            val lottieAnimation =
                result.weather[Constants.INDEX_WEATHER].main.uppercase().getWeatherStateAnimation()
            weather.setAnimation(lottieAnimation)

        }
    }


}
