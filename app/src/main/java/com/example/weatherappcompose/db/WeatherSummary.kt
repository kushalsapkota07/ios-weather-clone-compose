package com.example.weatherappcompose.db

data class WeatherSummary(
    val current_json: String,
    val daily_json: String,

)

data class WeatherImageCode(
    val isDay: Int=1,
    val weatherCode: Int=0
)