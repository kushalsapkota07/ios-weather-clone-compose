package com.example.weatherappcompose.ktor_api

import com.example.weatherappcompose.open_meteo.OpenWeatherResponse


interface OpenWeatherService {

    suspend fun getCurrentWeather(): OpenWeatherResponse
}