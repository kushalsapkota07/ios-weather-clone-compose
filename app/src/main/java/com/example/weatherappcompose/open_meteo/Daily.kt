package com.example.weatherappcompose.open_meteo

import kotlinx.serialization.Serializable

@Serializable
data class Daily(
    val daylight_duration: List<Double>,
    val precipitation_probability_max: List<Int>,
    val precipitation_sum: List<Double>,
    val sunrise: List<String>,
    val sunset: List<String>,
    val sunshine_duration: List<Double>,
    val temperature_2m_max: List<Double>,
    val temperature_2m_min: List<Double>,
    val time: List<String>,
    val uv_index_clear_sky_max: List<Double>,
    val uv_index_max: List<Double>,
    val weather_code: List<Int>
)