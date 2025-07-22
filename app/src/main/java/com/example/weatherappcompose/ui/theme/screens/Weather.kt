package com.example.weatherappcompose.ui.theme.screens

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherappcompose.R
import com.example.weatherappcompose.db.SimpleLocation
import com.example.weatherappcompose.ui.theme.states.UiState
import com.example.weatherappcompose.open_meteo.OpenWeatherResponse
import com.example.weatherappcompose.open_sdk.helper.getDailyForecast
import com.example.weatherappcompose.ui.theme.components.CurrentWeatherCollapsed
import com.example.weatherappcompose.ui.theme.components.CurrentWeatherExpanded
import com.example.weatherappcompose.ui.theme.components.CurrentWeatherLoading
import com.example.weatherappcompose.ui.theme.components.MainContent
import com.example.weatherappcompose.viewmodel.ConnectivityViewModel
import com.example.weatherappcompose.viewmodel.LocationViewModel
import com.example.weatherappcompose.wmo.getWeatherCondition
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlin.math.roundToInt

@Composable
fun WeatherScreenWrapper(
    simpleLocation: SimpleLocation,
    weatherState: UiState<OpenWeatherResponse>,
    connectivityViewModel: ConnectivityViewModel,
    locationViewModel: LocationViewModel,
    navigatedFromSearch: Boolean,
    imageResource: Int
){
    println("Navigated From Weather Value: $navigatedFromSearch")

    LaunchedEffect(Unit) {
        if(navigatedFromSearch){
            locationViewModel.getCurrentWeather(simpleLocation.lat,simpleLocation.lon)
        }
    }

    val isConnected by connectivityViewModel.isConnected.collectAsState()
    if (navigatedFromSearch) {
        WeatherScreen(
            isConnected,
            true,
            simpleLocation,
            weatherState,
            imageResource
        )
    } else{
        LaunchedEffect(Unit) {


            locationViewModel.getCurrentWeatherDbValue(simpleLocation.lat, simpleLocation.lon)
        }
        val weatherStateCopy by locationViewModel.weatherDbValue.collectAsState()

        WeatherScreen(
            isConnected,
            false,
            simpleLocation,
            weatherStateCopy,
            imageResource
        )
    }
}

@SuppressLint("ResourceType", "StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    isConnected: Boolean,
    navigatedFromSearch: Boolean,
    simpleLocation: SimpleLocation,
    weatherState: UiState<OpenWeatherResponse>,
    imageResource: Int = R.raw.sky_place_holder
) {
    val expandedAppBarHeight = 56.dp
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        snapAnimationSpec = tween()
    )
    val scrollState = scrollBehavior.state
    // app bar expanded state
    // collapsedFraction 1f = collapsed
    // collapsedFraction 0f = expanded

    val appBarCollapsed1 by remember {

        derivedStateOf { scrollState.collapsedFraction < 0.3f }
    }
    val appBarCollapsed2 by remember {
        derivedStateOf { scrollState.collapsedFraction < 0.4f }
    }
    val appBarCollapsed by remember {

        derivedStateOf { scrollState.collapsedFraction < 1f }
    }
    val headerTranslation = (expandedAppBarHeight)
    val listState = rememberLazyListState()

    val allowExpand = remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0 &&
                    listState.firstVisibleItemScrollOffset == 0
        }
    }

    val controlledNestedScroll = remember(allowExpand.value) {
        if (allowExpand.value) {
            scrollBehavior.nestedScrollConnection
        } else {
            object : NestedScrollConnection {
                // Prevent top bar from expanding
                override fun onPreScroll(
                    available: Offset,
                    source: NestedScrollSource
                ): Offset = Offset.Zero
            }
        }
    }
    val hazeState = rememberHazeState()

    val image = MutableStateFlow(imageResource)

    Box(

    ) {

        Crossfade(
            targetState = image.collectAsState().value,
            animationSpec = tween(durationMillis = 300),
            label = "image_crossfade"
        ) { imageRes ->

            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxSize()
                    .hazeSource(state = hazeState)

            )
        }

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                ) {
                    TopAppBar(
                        modifier = Modifier,
                        title = {
                            AnimatedVisibility(
                                visible = appBarCollapsed,
                                enter = fadeIn(animationSpec = tween()),
                                exit = fadeOut(animationSpec = tween())
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                }
                            }

                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            // set background color for expanded state to transparent
                            containerColor = Color.Transparent,
                            // set background color for collapsed state to transparent
                            scrolledContainerColor = Color.Transparent,
                            // set navigation icon color
                            navigationIconContentColor = MaterialTheme.colorScheme.onTertiary
                        )
                    )
                    TopAppBar(
                        modifier = Modifier,
                        title = {
                            AnimatedVisibility(
                                visible = appBarCollapsed,
                                enter = fadeIn(animationSpec = tween()),
                                exit = fadeOut(animationSpec = tween())
                            ) {
                                Column(
                                    modifier = Modifier.graphicsLayer {
                                        translationY =
                                            scrollState.collapsedFraction / headerTranslation.toPx()
                                    },
                                    verticalArrangement = Arrangement.SpaceEvenly,

                                    ) {

                                }

                                // extra space at the bottom of the header
                                // use spacer to prevent content being clipped
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            // set background color for expanded state to transparent
                            containerColor = Color.Transparent,
                            // set background color for collapsed state to transparent
                            scrolledContainerColor = Color.Transparent
                        ),
                        expandedHeight = expandedAppBarHeight,
                        windowInsets = WindowInsets(0, 0, 0, 0),
                        scrollBehavior = scrollBehavior
                    )
                }
            },
            modifier = Modifier.nestedScroll(controlledNestedScroll)
        ) { paddingValues ->

            when (weatherState) {

                is UiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(if (navigatedFromSearch) PaddingValues(0.dp) else paddingValues)
                    ) {
                        CurrentWeatherLoading(
                            location = simpleLocation.name
                        )
                        if(!isConnected) {

                            Text(
                                text = "No internet connection",
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                                modifier = Modifier.align(Alignment.Center),
                                color = Color.White
                            )
                        }

                    }

                }

                is UiState.Success -> {

                    val weather: OpenWeatherResponse = weatherState.value
                    val dailyForecast = getDailyForecast(weather)
                    val minTemp = dailyForecast[0].minTemp
                    val maxTemp = dailyForecast[0].maxTemp
                    val weatherCondition = getWeatherCondition(
                        weather.current.weather_code,
                        weather.current.is_day == 1
                    )
                    image.value = weatherCondition.image
                    Column(
                        modifier = Modifier.padding(if (navigatedFromSearch) PaddingValues(0.dp) else paddingValues)
                    ) {
                        if (!appBarCollapsed) {
                            CurrentWeatherCollapsed(
                                location = simpleLocation.name,
                                description = weatherCondition.description,
                                temperature = weather.current.temperature_2m.roundToInt(),
                                visible = appBarCollapsed,
                            )
                        } else {
                            CurrentWeatherExpanded(
                                isHome = true,
                                location = simpleLocation.name,
                                temperature = weather.current.temperature_2m.roundToInt(),
                                description = weatherCondition.description,
                                minTemperature = minTemp,
                                maxTemperature = maxTemp,
                                appBarCollapsed = appBarCollapsed,
                                appBarCollapsed1 = appBarCollapsed1,
                                appBarCollapsed2 = appBarCollapsed2
                            )
                        }
                        MainContent(
                            modifier = Modifier,
                            weather = weather,
                            paddingValues = paddingValues,
                            hazeState = hazeState,
                            lazyListState = listState
                        )
                    }
                }

                is UiState.Error -> {
                    println("Error: ${weatherState.message}")
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(if (navigatedFromSearch) PaddingValues(0.dp) else paddingValues)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceAround
                        ) {
                            Text(
                                text = simpleLocation.name,
                                fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                fontSize = 40.sp,
                                color = Color.White
                            )
                            Text(
                                text = "--",
                                fontWeight = FontWeight.Thin,
                                fontFamily = FontFamily(Font(R.font.sf_pro_display_thin)),
                                fontSize = 100.sp,
                                modifier = Modifier,
                                color = Color.White
                            )
                        }
                        if(!isConnected) {
                            Text(
                                text = "No internet connection",
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                                modifier = Modifier.align(Alignment.Center),
                                color = Color.White
                            )
                        }
                    }
                }
            }

        }
    }
}

