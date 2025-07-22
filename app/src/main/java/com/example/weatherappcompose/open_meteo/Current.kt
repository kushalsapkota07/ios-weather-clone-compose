package com.example.weatherappcompose.open_meteo

import kotlinx.serialization.Serializable

@Serializable
data class Current(
    val apparent_temperature: Double,
    val interval: Int,
    val is_day: Int,
    val precipitation: Double,
    val rain: Double,
    val snowfall: Double,
    val surface_pressure: Double,
    val temperature_2m: Double,
    val time: String,
    val weather_code: Int,
    val wind_direction_10m: Int,
    val wind_gusts_10m: Double,
    val wind_speed_10m: Double
)