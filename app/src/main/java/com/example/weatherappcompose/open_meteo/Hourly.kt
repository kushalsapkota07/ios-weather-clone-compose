package com.example.weatherappcompose.open_meteo

import kotlinx.serialization.Serializable

@Serializable
data class Hourly(
    val apparent_temperature: List<Double>,
    val dew_point_2m: List<Double>,
    val is_day: List<Int>,
    val precipitation: List<Double>,
    val precipitation_probability: List<Int>,
    val rain: List<Double>,
    val relative_humidity_2m: List<Int>,
    val sunshine_duration: List<Double>,
    val temperature_1000hPa: List<Double>,
    val temperature_2m: List<Double>,
    val time: List<String>,
    val uv_index: List<Double>,
    val visibility: List<Double>,
    val weather_code: List<Int>,
    val wind_gusts_10m: List<Double>,
    val wind_speed_10m: List<Double>
)