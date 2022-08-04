package com.example.mikmok.data.models
import com.cheese.weatherapp.models.*

data class Weathers(
    val coord: Coord,
    val weather: List<Weather>,
    val base: String,
    val main: Main,
    val visibility: Long,
    val wind: Wind,
    val rain :Rain,
    val clouds: Clouds,
    val dt: Long,
    val sys: Sys,
    val timezone: Long,
    val id: Long,
    val name: String,
    val cod: Int,
    val message: String,
)

/*
*
* {
    "coord": {
        "lon": 28.9833,
        "lat": 41.0351
    },
    "weather": [
        {
            "id": 800,
            "main": "Clear",
            "description": "clear sky",
            "icon": "01n"
        }
    ],
    "base": "stations",
    "main": {
        "temp": 296.83,
        "feels_like": 297.16,
        "temp_min": 294.24,
        "temp_max": 296.83,
        "pressure": 1013,
        "humidity": 73
    },
    "visibility": 10000,
    "wind": {
        "speed": 3.09,
        "deg": 60
    },
    "clouds": {
        "all": 0
    },
    "dt": 1659472435,
    "sys": {
        "type": 1,
        "id": 6970,
        "country": "TR",
        "sunrise": 1659409234,
        "sunset": 1659460801
    },
    "timezone": 10800,
    "id": 745042,
    "name": "Istanbul",
    "cod": 200
}
* */

