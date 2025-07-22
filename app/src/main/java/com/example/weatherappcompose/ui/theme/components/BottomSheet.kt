package com.example.weatherappcompose.ui.theme.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.weatherappcompose.R
import com.example.weatherappcompose.db.SimpleLocation
import com.example.weatherappcompose.db.WeatherResponse
import com.example.weatherappcompose.db.toEntity
import com.example.weatherappcompose.open_meteo.OpenWeatherResponse
import com.example.weatherappcompose.ui.theme.screens.Route
import com.example.weatherappcompose.ui.theme.screens.WeatherScreenWrapper
import com.example.weatherappcompose.ui.theme.states.UiState
import com.example.weatherappcompose.viewmodel.ConnectivityViewModel
import com.example.weatherappcompose.viewmodel.LocationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherBottomSheet(
    name: String,
    lat: Double,
    long: Double,
    locationViewModel: LocationViewModel,
    connectivityViewModel: ConnectivityViewModel,
    onNavigateToHomeScreen: (Route.HomeRoute) -> Unit
) {

    var navigateFromSearch by remember { mutableStateOf(true) }

    locationViewModel.getCurrentWeather(lat, long)
    locationViewModel.getWeatherImageResource(lat,long)

    val weatherState: UiState<OpenWeatherResponse> by locationViewModel.weatherState.collectAsState()

    Scaffold {

        val imageResource  = R.raw.sky_place_holder

        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )

        var isSheetOpen by remember { mutableStateOf(true) }

        if (isSheetOpen) {
            ModalBottomSheet(
                modifier = Modifier
                    .padding(it)
                    .fillMaxWidth(),
                sheetState = sheetState,
                onDismissRequest = {
                    isSheetOpen = false
                    onNavigateToHomeScreen(Route.HomeRoute(false))
                },
                dragHandle = {

                }
            ) {
                Box(
                ) {

                    WeatherScreenWrapper(
                        simpleLocation = SimpleLocation(
                            name = name.substringBefore(','),
                            lat = lat,
                            lon = long
                        ),
                        weatherState = weatherState,
                        navigatedFromSearch = true,
                        locationViewModel = locationViewModel,
                        connectivityViewModel = connectivityViewModel,
                        imageResource = imageResource,
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = {
                                isSheetOpen = false
                                onNavigateToHomeScreen(Route.HomeRoute(false))
                                Log.d("Message", "Weather Data not saved")
                            }
                        ) {
                            Icon(
                                modifier = Modifier.size(32.dp),
                                painter = painterResource(R.drawable.outline_cancel_24),
                                contentDescription = "Cancel",
                                tint = Color.White
                            )
                        }
                        IconButton(
                            onClick = {
                                isSheetOpen = false
                                navigateFromSearch = true


                                when (weatherState) {
                                    is UiState.Success -> {
                                        val response: WeatherResponse =
                                            (weatherState as UiState.Success).value.toEntity(
                                                lat,
                                                long
                                            )
                                        locationViewModel.saveLocationAndWeather(
                                            SimpleLocation(
                                                name = name.substringBefore(','),
                                                lat = lat,
                                                lon = long,
                                            ),
                                            response
                                        )
                                    }

                                    else -> {

                                    }
                                }
                                onNavigateToHomeScreen(Route.HomeRoute(true))
                            }
                        ) {
                            Icon(
                                modifier = Modifier.size(32.dp),
                                painter = painterResource(R.drawable.outline_add_circle_24),
                                contentDescription = "Save",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
