package com.example.mikmok.util

fun Double.toCelsius () : Double {
    return String.format("%.2f", (this - 273.15)).toDouble()
}

fun Double.toPercent () : Double {
    return String.format("%.2f", (this/100)).toDouble()
}