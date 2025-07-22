package com.example.weatherappcompose.open_meteo

import kotlinx.serialization.Serializable

@Serializable
data class CurrentUnits(
    val apparent_temperature: String,
    val interval: String,
    val is_day: String,
    val precipitation: String,
    val rain: String,
    val snowfall: String,
    val surface_pressure: String,
    val temperature_2m: String,
    val time: String,
    val weather_code: String,
    val wind_direction_10m: String,
    val wind_gusts_10m: String,
    val wind_speed_10m: String
)