package com.example.weatherappcompose.open_sdk.helper

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import com.example.weatherappcompose.R
import com.example.weatherappcompose.open_meteo.OpenWeatherResponse
import com.example.weatherappcompose.util.DarkBlue
import com.example.weatherappcompose.util.Green
import com.example.weatherappcompose.util.LightBlue
import com.example.weatherappcompose.util.Orange
import com.example.weatherappcompose.util.Red
import com.example.weatherappcompose.util.Yellow
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import kotlin.math.roundToInt

data class DailyValues(val day: String,
    val weatherCode: Int,
    val minTemp: Int,
    val maxTemp: Int,
    val chancesOfRain: Int,
    val uvIndex: Double,
    val precipitation_sum: Double,
    val precipitation_sum_tomorrow: Double,
    val sunrise: String,
    val sunset: String,
    )

fun getDailyForecast(response: OpenWeatherResponse): List<DailyValues>{

    val dailyForecastValues: MutableList<DailyValues> = mutableListOf()
    val daysOfWeek = response.daily.time.map { dateString ->
        val date = LocalDate.parse(dateString)
        date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
    }

    //The api gives 14 day forecast response but we only need 10

    for (i:Int in 0..9){
        dailyForecastValues.add(
            DailyValues(
                daysOfWeek[i],
                response.daily.weather_code[i],
                response.daily.temperature_2m_min[i].roundToInt(),
                response.daily.temperature_2m_max[i].roundToInt(),
                response.daily.precipitation_probability_max[i],
                response.daily.uv_index_max[0],
                response.daily.precipitation_sum[0],
                response.daily.precipitation_sum[1],
                response.daily.sunrise[0],
                response.daily.sunset[0]
            )
        )
    }

    return dailyForecastValues
}

fun getTemperatureGradientColors(minTemp: Int, maxTemp: Int): List<Color> {
    // Helper function to get color for a specific temperature
    fun getColorForTemp(temp: Int): Color {
        return when {
            temp < 0 -> DarkBlue          // Extremely Cold
            temp in 0..15 -> LightBlue  // Cold
            temp in 15..20 -> Green     // Slightly Cold
            temp in 20..25 -> Yellow    // Slightly Warm
            temp in 25..30 -> Orange    // Warm
            else -> Red                   // Extremely Hot (30°C and above)
        }
    }

    // Get colors for min and max temperatures
    val startColor = getColorForTemp(minTemp)
    val endColor = getColorForTemp(maxTemp)

    // If min and max are in the same range, use that color only
    if (startColor == endColor) {
        return listOf(startColor)
    }

    // If min and max span multiple ranges, interpolate colors
    // For simplicity, we'll use two colors (start and end) for the gradient
    return listOf(startColor, endColor)
}

//fun Int.toUvStrength(
//    context: Context
//) : Array<String>{
//    return when {
//        this in 0..2 -> context.resources.getStringArray(R.array.uv_index_low)
//        this in 3..5 -> context.resources.getStringArray(R.array.uv_index_moderate)
//        this in 6..7 -> context.resources.getStringArray(R.array.uv_index_high)
//        this in 8..10 -> context.resources.getStringArray(R.array.uv_index_very_high)
//        this > 10 -> context.resources.getStringArray(R.array.uv_index_extreme)
//        else -> context.resources.getStringArray(R.array.uv_index_low)
//    }
//}
