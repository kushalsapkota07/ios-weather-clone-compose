package com.example.weatherappcompose.ui.theme.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.weatherappcompose.R
import com.example.weatherappcompose.open_meteo.OpenWeatherResponse
import com.example.weatherappcompose.open_sdk.helper.getDailyForecast
import com.example.weatherappcompose.open_sdk.helper.getHourlyForecast
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import kotlin.math.roundToInt

@Composable
fun MainContent(
    modifier: Modifier,
    weather: OpenWeatherResponse,
    paddingValues: PaddingValues,
    lazyListState: LazyListState,
    hazeState: HazeState,
    ) {
    val hourlyForecast = getHourlyForecast(weather)
    val dailyForecast = getDailyForecast(weather)
    val uvIndex: Int = hourlyForecast[0].uvIndex //For current day
    val precipitation: Int = dailyForecast[0].precipitation_sum.roundToInt()
    val precipitationTomorrow: Int = dailyForecast[0].precipitation_sum_tomorrow.roundToInt()
    val overallMin = dailyForecast.minBy { it.minTemp }.minTemp //For 10 day
    val overallMax = dailyForecast.maxBy { it.maxTemp }.maxTemp //For 10 day

    LazyColumn(
            modifier = modifier,
            state = lazyListState,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            stickyHeader {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(.9f)
                        .clip(RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp))
                        .padding()
                        .background(Color(0xFF1C1C1E).copy(alpha = 0.6f)) // Dark translucent background
                        .hazeEffect(
                            hazeState
                        )
                        .height(40.dp),
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    Icon(
                        modifier = Modifier.padding(10.dp),
                        painter = painterResource(id = R.drawable.baseline_access_time_24),
                        contentDescription = "Hourly Forecast Icon",
                        tint = Color.White
                    )
                    Text(text = "HOURLY FORECAST", color = Color.White)
                }
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp), // Aligned with content
                    thickness = 0.5.dp, // Thin line like iOS
                    color = Color.White.copy(alpha = 0.2f) // Light, subtle divider
                )
            }
            item {
                HourlyForecast(
                    hourlyForecast,
                    hazeState
                )
            }

            item {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                )
            }

            stickyHeader {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(.9f)
                        .clip(RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp))
                        .padding()
                        .background(Color(0xFF1C1C1E).copy(alpha = 0.6f)) // Dark translucent background
                        .hazeEffect(
                            hazeState
                        )
                        .height(40.dp),
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    Icon(
                        modifier = Modifier.padding(10.dp),
                        painter = painterResource(id = R.drawable.baseline_access_time_24),
                        contentDescription = "10-Day Forecast Icon",
                        tint = Color.White
                    )
                    Text(text = "10-DAY FORECAST", color = Color.White)
                }
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp), // Aligned with content
                    thickness = 0.5.dp, // Thin line like iOS
                    color = Color.White.copy(alpha = 0.2f) // Light, subtle divider
                )
            }
            item {
                TenDayForecast(
                    dailyForecast,
                    weather.current.temperature_2m.roundToInt(),
                    overallMin,
                    overallMax,
                    hazeState
                )
            }

            item {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                )
            }
            item {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                )
            }

            stickyHeader {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(.9f)
                        .height(40.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)

                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(.5f)
                            .clip(RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp))
                            .padding()
                            .background(Color(0xFF1C1C1E).copy(alpha = 0.6f)) // Dark translucent background
                            .hazeEffect(
                                hazeState
                            )
                            .weight(1f),
                        verticalAlignment = Alignment.CenterVertically,

                        ) {
                        Icon(
                            modifier = Modifier.padding(10.dp),
                            painter = painterResource(id = R.drawable.baseline_access_time_24),
                            contentDescription = "Feels Like Icon",
                            tint = Color.White
                        )
                        Text(
                            text = "FEELS LIKE",
                            color = Color.White
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(.5f)
                            .clip(RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp))
                            .padding()
                            .background(Color(0xFF1C1C1E).copy(alpha = 0.6f)) // Dark translucent background
                            .hazeEffect(
                                hazeState
                            )
                            .weight(1f),
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        Icon(
                            modifier = Modifier.padding(10.dp),
                            painter = painterResource(id = R.drawable.baseline_access_time_24),
                            contentDescription = "UV Index Icon",
                            tint = Color.White
                        )
                        Text(
                            text = "UV INDEX",
                            color = Color.White
                        )
                    }
                }
            }

            item {
                Group1(
                    apparentTemperature = hourlyForecast[0].apparentTemperature,
                    uvIndex = uvIndex,
                    hazeState = hazeState
                )
            }
            item {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                )
            }

            stickyHeader {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(.9f)
                        .height(40.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)

                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(.5f)
                            .clip(RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp))
                            .padding()
                            .background(Color(0xFF1C1C1E).copy(alpha = 0.6f)) // Dark translucent background
                            .hazeEffect(
                                hazeState
                            )
                            .weight(1f),
                        verticalAlignment = Alignment.CenterVertically,

                        ) {
                        Icon(
                            modifier = Modifier.padding(10.dp),
                            painter = painterResource(id = R.drawable.baseline_access_time_24),
                            contentDescription = "Visibility Icon",
                            tint = Color.White
                        )
                        Text(
                            text = "VISIBILITY",
                            color = Color.White
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(.5f)
                            .clip(RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp))
                            .padding()
                            .background(Color(0xFF1C1C1E).copy(alpha = 0.6f)) // Dark translucent background
                            .hazeEffect(
                                hazeState
                            )
                            .weight(1f),
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        Icon(
                            modifier = Modifier.padding(10.dp),
                            painter = painterResource(id = R.drawable.baseline_access_time_24),
                            contentDescription = "Precipitation Icon",
                            tint = Color.White
                        )
                        Text(
                            text = "PRECIPITATION",
                            color = Color.White
                        )
                    }
                }
            }

            item {
                Group2(
                    visibility = hourlyForecast[0].visibility,
                    precipitation = listOf(precipitation,precipitationTomorrow),
                    hazeState = hazeState
                )
            }
            item {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                )
            }
            stickyHeader {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(.9f)
                        .height(40.dp)

                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp))
                            .padding()
                            .background(Color(0xFF1C1C1E).copy(alpha = 0.6f)) // Dark translucent background
                            .hazeEffect(
                                hazeState
                            )
                            .weight(1f),
                        verticalAlignment = Alignment.CenterVertically,

                        ) {
                        Icon(
                            modifier = Modifier.padding(10.dp),
                            painter = painterResource(id = R.drawable.baseline_air_24),
                            contentDescription = "Wind Icon",
                            tint = Color.White
                        )
                        Text(
                            text = "WIND",
                            color = Color.White
                        )
                    }
                }
            }
            item {
                Compass(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    wind = weather.current.wind_speed_10m.roundToInt(),
                    gusts = weather.current.wind_gusts_10m.roundToInt(),
                    direction = weather.current.wind_direction_10m,
                    hazeState = hazeState
                )
            }
        }

}

