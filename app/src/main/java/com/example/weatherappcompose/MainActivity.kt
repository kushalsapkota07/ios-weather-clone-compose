package com.example.weatherappcompose

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.weatherappcompose.db.SimpleLocation
import com.example.weatherappcompose.ui.theme.components.WeatherBottomSheet
import com.example.weatherappcompose.ui.theme.screens.HomeScreen
import com.example.weatherappcompose.ui.theme.screens.Route
import com.example.weatherappcompose.ui.theme.screens.SearchScreen
import com.example.weatherappcompose.ui.theme.screens.WeatherScreenWrapper
import com.example.weatherappcompose.ui.theme.states.UiState
import com.example.weatherappcompose.viewmodel.ConnectivityViewModel
import com.example.weatherappcompose.viewmodel.LocationViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainActivity : ComponentActivity() {

    @SuppressLint("CoroutineCreationDuringComposition", "UnusedMaterial3ScaffoldPaddingParameter",
        "StateFlowValueCalledInComposition"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val connectivityViewModel = viewModel<ConnectivityViewModel> {
                ConnectivityViewModel(
                    connectivityObserver = AndroidConnectivityObserver(
                        context = applicationContext
                    )
                )
            }
            val locationViewModel = getViewModel<LocationViewModel>()

            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Route.HomeRoute()
            ){
                composable<Route.HomeRoute> {
                    HomeScreen(
                        locationViewModel=locationViewModel,
                        onNavigateToWeather = {
                            navController.navigate(Route.WeatherRoute(it.name,it.latitude,it.longitude,false))
                        },
                        onNavigateToBottomSheetWeatherScreen = {
                            navController.navigate(Route.BottomSheetWeatherRoute(it.name,it.latitude,it.longitude))
                        },
                        onNavigateToSearchScreen = {
                            navController.navigate(Route.SearchRoute(""))
                        },
                        connectivityViewModel = connectivityViewModel
                    )
                }
                composable<Route.WeatherRoute> {

                    val arguments = it.toRoute<Route.WeatherRoute>()
                    locationViewModel.getWeatherImageResource(arguments.latitude,arguments.longitude)


                    WeatherScreenWrapper(
                        simpleLocation = SimpleLocation(
                            name = arguments.name,
                            lat = arguments.latitude,
                            lon = arguments.longitude
                        ),
                        weatherState = UiState.Loading(),
                        navigatedFromSearch = false,
                        locationViewModel = locationViewModel,
                        connectivityViewModel = connectivityViewModel,
                        imageResource =  R.raw.sky_place_holder
                    )
                }
                
                composable<Route.BottomSheetWeatherRoute> {
                    val arguments = it.toRoute<Route.BottomSheetWeatherRoute>()
                    WeatherBottomSheet(
                        name = arguments.name,
                        lat = arguments.latitude,
                        long = arguments.longitude,
                        locationViewModel = locationViewModel,
                        connectivityViewModel = connectivityViewModel,
                        onNavigateToHomeScreen = {
                            navController.navigate(Route.HomeRoute())
                        }
                    )
                }

                composable<Route.SearchRoute> { it ->
                    val arguments = it.toRoute<Route.SearchRoute>()
                    SearchScreen(
                        locationViewModel = locationViewModel,
                        connectivityViewModel = connectivityViewModel,
                        onNavigateToBottomSheetWeatherScreen = {
                            navController.navigate(Route.BottomSheetWeatherRoute(
                                it.name,
                                it.latitude,
                                it.longitude
                            ))
                        },
                        onNavigateToSearchScreen = {
                            navController.navigate(Route.SearchRoute(""))
                        }
                    )
                }
            }
        }
    }
}


