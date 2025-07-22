package com.example.weatherappcompose.ui.theme.screens

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.weatherappcompose.ui.theme.components.SearchBarSample
import com.example.weatherappcompose.viewmodel.ConnectivityViewModel
import com.example.weatherappcompose.viewmodel.LocationViewModel

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    connectivityViewModel: ConnectivityViewModel,
    locationViewModel: LocationViewModel,
    onNavigateToBottomSheetWeatherScreen: (Route.BottomSheetWeatherRoute) -> Unit,
    onNavigateToSearchScreen: (Route.SearchRoute) -> Unit
){
    SearchBarSample(
        modifier = Modifier,
        connectivityViewModel = connectivityViewModel,
        locationViewModel = locationViewModel,
        onNavigateToBottomSheetWeatherScreen = onNavigateToBottomSheetWeatherScreen,
        onNavigateToSearchScreen = onNavigateToSearchScreen
    )
}

