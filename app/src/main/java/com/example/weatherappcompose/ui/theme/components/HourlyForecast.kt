package com.example.weatherappcompose.ui.theme.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherappcompose.open_sdk.helper.HourlyForecast
import com.example.weatherappcompose.wmo.getWeatherCondition
import dev.chrisbanes.haze.HazeDefaults
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.HazeMaterials
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale


//This will be shown if the response contains no forecastday field in the json response
@Composable
fun HourlyForecastNoData() {
    //Hourly Forecast Header
    Text(
        text = "No Data Available",
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

@Composable
fun HourlyForecastItem(
    hour: String,
    weatherCode: Int,
    temperature: String,
    contentDescription: String
) {
    val image = getWeatherCondition(weatherCode, true).drawable
    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .fillMaxHeight(.3f),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = hour,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 0.dp, vertical = 10.dp),
            color = Color.White
        )
        Image(
            painterResource(id = image),
            contentDescription = "Weather Icon",
            modifier = Modifier
                .size(26.dp)
        )
        Text(
            text = temperature,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 0.dp, vertical = 10.dp),
            color = Color.White
        )
    }
}

@Composable
fun HourlyForecast(
    forecastHour: List<HourlyForecast>,
    hazeState: HazeState
) {


    LazyRow(
        modifier = Modifier
            .fillMaxWidth(.9f)
            .clip(RoundedCornerShape(0.dp, 0.dp, 16.dp, 16.dp))
            .background(Color(0xFF1C1C1E).copy(alpha = 0.6f)) // Dark translucent background
            .hazeEffect(
                hazeState
            )

    ) {
        itemsIndexed(forecastHour) { index, item ->
            HourlyForecastItem(
                hour = if (index == 0) "Now" else item.time.toUTC(),
                weatherCode = item.weatherCode,
                temperature = "${item.temperature}",
                contentDescription = item.contentDescription
            )
        }
    }
}

fun String.toUTC(): String {
    val time = LocalDateTime.parse(this) //e.g., "2025-04-08T15:00"
    val hour = time.format(DateTimeFormatter.ofPattern("h", Locale.ENGLISH))
    val amPm = time.format(DateTimeFormatter.ofPattern("a", Locale.ENGLISH))
        .uppercase(Locale.getDefault())
    return hour + amPm
}

fun String.toLong(): Long {
    val time = LocalDateTime.parse(this)
    val timeInSeconds = time.toEpochSecond(ZoneOffset.UTC)
    return timeInSeconds
}