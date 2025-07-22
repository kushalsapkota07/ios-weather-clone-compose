package com.example.weatherappcompose.ui.theme.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEachIndexed
import com.example.weatherappcompose.R
import com.example.weatherappcompose.open_sdk.helper.DailyValues
import com.example.weatherappcompose.open_sdk.helper.getTemperatureGradientColors
import com.example.weatherappcompose.wmo.getWeatherCondition
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource

@Composable
fun TenDayForecast(
    daily: List<DailyValues>,
    currentTemp: Int,
    overallMin: Int,
    overallMax: Int,
    hazeState: HazeState
) {

    Column(
        modifier = Modifier
            .fillMaxWidth(.9f)
            .clip(RoundedCornerShape(0.dp, 0.dp, 16.dp, 16.dp))
            .hazeEffect(state = hazeState)
    ) {

        daily.fastForEachIndexed { index, _ ->
            ForecastDayItem(
                day = daily[index].day,
                weatherCode = daily[index].weatherCode,
                minTemp = daily[index].minTemp,
                maxTemp = daily[index].maxTemp,
                overallMin = overallMin,
                overallMax = overallMax,
                range = overallMax - overallMin,
                colors = getTemperatureGradientColors(
                    daily[index].minTemp,
                    daily[index].maxTemp
                ),
                chanceOfRain = daily[index].chancesOfRain // Rain %
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp), // Aligned with content
                thickness = 0.5.dp, // Thin line like iOS
                color = Color.White.copy(alpha = 0.2f) // Light, subtle divider
            )

        }
    }
}

@Composable
fun ForecastDayItem(
    day: String,
    weatherCode: Int,
    minTemp: Int,
    maxTemp: Int,
    overallMin: Int,
    overallMax: Int,
    range: Int,
    colors: List<Color>,
    chanceOfRain: Int
) {
    val startFraction = if (range > 0) (minTemp - overallMin) / range.toFloat() else 0f
    val endFraction = if (range > 0) (maxTemp - overallMin) / range.toFloat() else 0f
    val icon = getWeatherCondition(weatherCode, true).drawable
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .height(36.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = day,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White.copy(alpha = 0.9f),
            modifier = Modifier.weight(1f)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (chanceOfRain > 0) {

                    Image(
                        painterResource(id = icon),
                        contentDescription = "Weather Icon",
                        modifier = Modifier
                            .size(24.dp),
                    )

                    Text(
                        text = "$chanceOfRain%",
                        fontSize = 12.sp,
                        color = Color.Cyan.copy(alpha = 0.9f),
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.sf_pro_display_medium))
                    )

                } else {

                    Image(
                        painterResource(id = icon),
                        contentDescription = "Weather Icon",
                        modifier = Modifier
                            .size(24.dp)
                    )

                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(2f)
        ) {
            Text(
                text = "${minTemp}°",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                color = Color.White.copy(alpha = 0.6f)
            )

            TemperatureBar(
                modifier = Modifier
                    .weight(2f)
                    .height(6.dp) // Slimmer for iOS-like style
                    .clip(RoundedCornerShape(3.dp))
                    .padding(horizontal = 6.dp),
                startFraction = startFraction,
                endFraction = endFraction,
                colors = colors
            )

            Text(
                text = "${maxTemp}°",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                color = Color.White.copy(alpha = 0.9f)
            )
        }
    }
}

@Composable
fun TemperatureBar(
    modifier: Modifier = Modifier,
    startFraction: Float, //Start position (0 to 1)
    endFraction: Float, //End position (0 to 1)
    colors: List<Color>
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(5.dp)
    ) {

        val canvasWidth = size.width
        val canvasHeight = size.height

        drawRoundRect(
            color = Color.LightGray,
            topLeft = Offset(0f, 0f),
            size = Size(canvasWidth, canvasHeight),
            cornerRadius = CornerRadius(10f, 10f)
        )

        val barStartX = startFraction * canvasWidth
        val barEndX = endFraction * canvasWidth

        val gradientColors: List<Color> = when {

            colors.isEmpty() -> listOf(Color.Gray, Color.Gray)
            colors.size == 1 -> listOf(colors[0], colors[0])
            else -> {
                colors
            }
        }
        drawRoundRect(
            brush = Brush.horizontalGradient(
                colors = gradientColors,
                startX = barStartX,
                endX = barEndX
            ),
            topLeft = Offset(barStartX, 0f),
            size = Size(barEndX - barStartX, canvasHeight),
            cornerRadius = CornerRadius(10f, 10f)
        )
    }
}

