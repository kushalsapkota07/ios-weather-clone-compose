package com.example.weatherappcompose.ui.theme.states

import com.example.weatherappcompose.R


// Using Generic parameter <T> allows us to represent different types of data in success
// While not using it keeps the type to only one for eg. WeatherResponse
sealed class UiState<T> {
    class Loading<T>: UiState<T>()
    data class Success<T>(val value: T): UiState<T>()
    data class Error<T>(val message: String?=null): UiState<T>()
}

data class DropDownItem(
    val icon: Int,
    val itemName: String,
)

val itemList = listOf(
    DropDownItem(R.drawable.baseline_edit_24,"Edit List"),
    DropDownItem(R.drawable.baseline_notifications_24,"Notifications"),
    DropDownItem(R.drawable.degree,"Celsius"),
    DropDownItem(R.drawable.fahrenheit,"Fahrenheit"),
    DropDownItem(R.drawable.baseline_bar_chart_24,"Units"),
    DropDownItem(R.drawable.baseline_report_24,"Report an Issue")
)