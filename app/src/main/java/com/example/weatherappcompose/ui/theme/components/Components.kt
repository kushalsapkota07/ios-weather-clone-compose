package com.example.weatherappcompose.ui.theme.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherappcompose.R
import com.example.weatherappcompose.util.Green
import com.example.weatherappcompose.util.Orange
import com.example.weatherappcompose.util.Red
import com.example.weatherappcompose.util.Yellow
import kotlin.math.atan2
import kotlin.math.hypot
import kotlin.math.roundToInt
import kotlin.math.sin


@Composable
fun FeelsLike(modifier: Modifier, apparentTemperature: Int) {
    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(10.dp, 0.dp, 10.dp, 10.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "${apparentTemperature}°",
                fontSize = 26.sp,
                fontFamily = FontFamily(Font(R.font.sf_pro_display_medium)),
                color = Color.White
            )
            Text(
                text = "Similar to actual temperature.",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                color = Color.White
            )
        }
    }
}

@Composable
fun UvIndex(modifier: Modifier, uvIndex: Int) {

    val description =  when {
        uvIndex in 0..2 -> stringArrayResource(R.array.uv_index_low)
        uvIndex in 3..5 -> stringArrayResource(R.array.uv_index_moderate)
        uvIndex in 6..7 -> stringArrayResource(R.array.uv_index_high)
        uvIndex in 8..10 -> stringArrayResource(R.array.uv_index_very_high)
        uvIndex > 10 -> stringArrayResource(R.array.uv_index_extreme)
        else -> stringArrayResource(R.array.uv_index_low)
    }
    Box(
        modifier = modifier

    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(10.dp, 0.dp, 10.dp, 10.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "$uvIndex",
                fontSize = 26.sp,
                fontFamily = FontFamily(Font(R.font.sf_pro_display_medium)),
                color = Color.White
            )
            Text(
                text = description[0],
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.sf_pro_display_medium)),
                color = Color.White
            )

            UVIndexBar(uvIndex = uvIndex.toFloat())
            Text(
                text = description[1],
                fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                color = Color.White,
            )
        }
    }
}


@Composable
fun UVIndexBar(
    modifier: Modifier = Modifier,
    uvIndex: Float, // UV Index value (e.g., 0 to 11+)
    maxUVIndex: Float = 11f,
    colors: List<Color> = listOf(Green, Yellow, Orange, Red)
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(16.dp)
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val barHeight = 6.dp.toPx()
        val barTop = canvasHeight / 2 - barHeight / 2

        // Gray Bar for reference

//        drawRoundRect(
//            brush = Brush.horizontalGradient(colors),
//            topLeft = Offset(0f, canvasHeight / 2 - 2.dp.toPx()),
//            size = Size(canvasWidth, 4.dp.toPx()),
//            cornerRadius = CornerRadius(10f, 10f)
//        )

        drawRoundRect(
            brush = Brush.horizontalGradient(colors),
            topLeft = Offset(0f, barTop),
            size = Size(canvasWidth, barHeight),
            cornerRadius = CornerRadius(10f, 10f)
        )

        // Clamp UV value between 0 and max
        val clampedUV = uvIndex.coerceIn(0f, maxUVIndex)
        val uvX = (clampedUV / maxUVIndex) * canvasWidth

        // Draw circle indicator
        drawCircle(
            color = Color.White,
            radius = 6.dp.toPx(),
            center = Offset(uvX, canvasHeight / 2)
        )
    }
}

@Composable
fun Precipitation(modifier: Modifier, precipitation: List<Int>) {
    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(10.dp, 0.dp, 10.dp, 10.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "${precipitation[0]} mm",
                fontSize = 26.sp,
                fontFamily = FontFamily(Font(R.font.sf_pro_display_medium)),
                color = Color.White
            )
            Text(
                text = stringResource(R.string.today),
                fontSize = 16.sp,
                color = Color.White
            )
            Text(
                text = "${precipitation[1]} mm expected tomorrow",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                color = Color.White,
            )
        }
    }
}

@Composable
fun Visibility(modifier: Modifier, visibility: Int) {

    val formattedVisibility = (visibility/1000).toDouble()
    val description = when {
        formattedVisibility <= 1.0 -> stringArrayResource(R.array.visibility_extremely_poor)
        formattedVisibility <= 2.0 -> stringArrayResource(R.array.visibility_very_poor)
        formattedVisibility <= 5.0 -> stringArrayResource(R.array.visibility_poor)
        formattedVisibility <= 10.0 -> stringArrayResource(R.array.visibility_moderate)
        formattedVisibility <= 20.0 -> stringArrayResource(R.array.visibility_good)
        formattedVisibility <= 40.0 -> stringArrayResource(R.array.visibility_very_good)
        formattedVisibility > 40.0 -> stringArrayResource(R.array.visibility_excellent)
        else -> stringArrayResource(R.array.visibility_undefined)
    }
    Box(
        modifier = modifier.padding(10.dp, 0.dp, 10.dp, 10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                ,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = "${formattedVisibility.roundToInt()} km",
                fontSize = 26.sp,
                fontFamily = FontFamily(Font(R.font.sf_pro_display_medium)),
                color = Color.White
            )
            Text(
                text = description[1],
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                color = Color.White
            )
        }
    }
}


@Composable
fun SunArcView(
    modifier: Modifier,
    hours: List<Long>, // 24 hrs of a day converted to UTC Long values
    sunrise: Long,
    sunset: Long,
    current: Long
) {


    val min = hours.min()
    val max = hours.max()

    val sunriseValue = (sunrise - min) / (max - min).toFloat()
    val sunsetValue = (sunset - min) / (max - min).toFloat()
    val currentValue = (current - min) / (max - min).toFloat()

    Box(modifier = modifier.padding(10.dp, 0.dp, 10.dp, 10.dp)) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val width = size.width
            val height = size.height

            // Draw sine arc path

            val arcPath = Path()
            val points = 10
            for (i in 0..points) {
                val fraction = i / points.toFloat()
                val x = width * fraction
                val yNorm = 0.4f + 0.4f * sin(fraction * Math.PI).toFloat()
                val y = height * (1f - yNorm)

                if (i == 0) {
                    arcPath.moveTo(x, y)
                } else {
                    arcPath.lineTo(x, y)
                }
            }

            // Where the sun currently is
            val currentX = width * currentValue
            val currentYNorm = 0.4 + 0.2f * sin(currentValue * Math.PI).toFloat()
            val currentY = height * (1f - currentYNorm).toFloat()

            val sunriseSunsetPath = Path()

            // Sunrise Point
            val sunriseX = width * sunriseValue
            val sunriseYNorm = 0.4 + 0.2f * sin(sunriseValue * Math.PI).toFloat()
            val sunriseY = height * (1f - sunriseYNorm).toFloat()

            // Sunset Point
            val sunsetX = width * sunsetValue
            val sunsetYNorm = 0.4 + 0.2f * sin(sunsetValue * Math.PI).toFloat()
            val sunsetY = height * (1f - sunsetYNorm).toFloat()

            val start = Offset(sunriseX, sunriseY)
            val end = Offset(sunsetX, sunsetY)

            // How much you want to extend the line beyond each end
            val extension = size.width

            // Direction vector
            val dx = end.x - start.x
            val dy = end.y - start.y
            val length = hypot(dx, dy)

            // Normalised direction
            val dirX = dx / length
            val dirY = dy / length

            // Extended points
            val extendedStart = Offset(start.x - dirX * extension, start.y - dirY * extension)
            val extendedEnd = Offset(end.x + dirX * extension, end.y + dirY * extension)

            val center = Offset(
                x = (start.x + end.x) / 2,
                y = (start.y + end.y) / 2
            )

            // Calculate the angle between the two points
            val deltaX = end.x - start.x
            val deltaY = end.y - start.y
            val angle = atan2(deltaY, deltaX) * (-180f / Math.PI).toFloat()

            rotate(degrees = angle, pivot = center) {

                drawPath(
                    path = arcPath,
                    color = Color.Gray,
                    style = Stroke(width = 4.dp.toPx())
                )

                drawLine(
                    color = Color.White,
                    start = extendedStart,
                    end = extendedEnd,
                    strokeWidth = 4f
                )

                sunriseSunsetPath.apply {
                    moveTo(sunriseX, sunriseY)
                    lineTo(sunsetX, sunsetY)
                }

                drawPath(
                    path = sunriseSunsetPath,
                    color = Color.White,
                    style = Stroke(width = 2.dp.toPx())
                )

                drawCircle(
                    color = Color.White,
                    radius = 6.dp.toPx(),
                    center = Offset(currentX, currentY)
                )
            }
        }
    }
}



