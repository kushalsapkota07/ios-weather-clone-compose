package com.example.weatherappcompose.ui.theme.screens

import kotlinx.serialization.Serializable

sealed class Route{

    @Serializable
    data class HomeRoute(
        val saveLocation: Boolean = false
    ): Route()

    @Serializable
    data class WeatherRoute(
        val name:String,
        val latitude: Double,
        val longitude: Double,
        val navigatedFromSearch: Boolean
    ): Route()

    @Serializable
    data class BottomSheetWeatherRoute(
        val name: String,
        val latitude: Double,
        val longitude: Double
    ): Route()

    @Serializable
    data class SearchRoute(
        val cityName: String
    )

}