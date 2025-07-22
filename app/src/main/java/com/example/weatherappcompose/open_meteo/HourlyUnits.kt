package com.example.weatherappcompose.open_meteo

import kotlinx.serialization.Serializable

@Serializable
data class HourlyUnits(
    val apparent_temperature: String,
    val dew_point_2m: String,
    val is_day: String,
    val precipitation: String,
    val precipitation_probability: String,
    val rain: String,
    val relative_humidity_2m: String,
    val sunshine_duration: String,
    val temperature_1000hPa: String,
    val temperature_2m: String,
    val time: String,
    val uv_index: String,
    val visibility: String,
    val weather_code: String,
    val wind_gusts_10m: String,
    val wind_speed_10m: String
)