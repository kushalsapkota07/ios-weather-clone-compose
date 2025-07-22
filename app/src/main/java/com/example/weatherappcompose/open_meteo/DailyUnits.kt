package com.example.weatherappcompose.open_meteo

import kotlinx.serialization.Serializable

@Serializable
data class DailyUnits(
    val daylight_duration: String,
    val precipitation_probability_max: String,
    val sunrise: String,
    val sunset: String,
    val sunshine_duration: String,
    val temperature_2m_max: String,
    val temperature_2m_min: String,
    val time: String,
    val uv_index_clear_sky_max: String,
    val uv_index_max: String,
    val weather_code: String
)