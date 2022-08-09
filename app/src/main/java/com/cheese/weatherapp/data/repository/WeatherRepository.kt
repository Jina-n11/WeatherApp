package com.cheese.weatherapp.data.repository

import com.cheese.weatherapp.data.State
import com.cheese.weatherapp.data.models.Weather
import com.cheese.weatherapp.data.services.BaseWeatherService
import io.reactivex.rxjava3.core.Observable

interface WeatherRepository {
    fun getWeatherByCityName(cityName: String): Observable<State<Weather>>
}

class WeatherRepositoryImp(private val weatherService: BaseWeatherService) : WeatherRepository {
    override fun getWeatherByCityName(cityName: String): Observable<State<Weather>> {
        return Observable.create { emit ->
            emit.onNext(State.Loading)
            emit.onNext(weatherService.getWeatherByCityName(cityName))
            emit.onComplete()
        }
    }
}