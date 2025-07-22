package com.example.weatherappcompose.ui.theme.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.atan2
import kotlin.math.hypot
import kotlin.math.sin


@Composable
fun SunArcViewS(
    modifier: Modifier,
    arcValues: List<Float>,
    sunrise: Float,
    sunset: Float,
    current: Float
) {

    val timeInSeconds: List<Float> = arcValues
    val min = timeInSeconds.min()
    val max = timeInSeconds.max()

    val sunriseValue = (sunrise - min) / (max - min)
    val sunsetValue = (sunset - min) / (max - min)
    val currentValue = (current - min) / (max - min)

    Box(modifier = Modifier
        .fillMaxWidth()
        .size(120.dp)) {

        Canvas(modifier = Modifier
            .fillMaxWidth(.3f)
            .height(120.dp)) {
            val width = size.width
            val height = size.height

            // Draw sine arc path

            val arcPath = Path()
            val points = 10
            for (i in 0..points) {
                val fraction = i / points.toFloat()
                val x = width * fraction
                val yNorm = 0.2f + 0.6f * sin(fraction * Math.PI).toFloat()
                val y = height * (1f - yNorm)

                if (i == 0) {
                    arcPath.moveTo(x, y)
                } else {
                    arcPath.lineTo(x, y)
                }
            }

            // Where the sun currently is
            val currentX = width * currentValue
            val currentYNorm = 0.2 + 0.6f * sin(currentValue * Math.PI).toFloat()
            val currentY = height * (1f - currentYNorm).toFloat()


            val sunriseSunsetPath = Path()

            // Sunrise Point
            val sunriseX = width * sunriseValue
            val sunriseYNorm = 0.2 + 0.6f * sin(sunriseValue * Math.PI).toFloat()
            val sunriseY = height * (1f - sunriseYNorm).toFloat()

            // Sunset Point
            val sunsetX = width * sunsetValue
            val sunsetYNorm = 0.2 + 0.6f * sin(sunsetValue * Math.PI).toFloat()
            val sunsetY = height * (1f - sunsetYNorm).toFloat()

            val start = Offset(sunriseX, sunriseY)
            val end = Offset(sunsetX, sunsetY)

            // How much you want to extend the line beyond each end
            val extension = size.width / 3

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

@Preview
@Composable
fun SunArcViewPreview() {
    SunArcViewS(
        modifier = Modifier.fillMaxSize(),
        arcValues = listOf(
            0.0f, 0.043f, 0.087f, 0.13f, 0.174f, 0.217f, 0.261f, 0.304f,
            0.348f, 0.391f, 0.435f, 0.478f, 0.522f, 0.565f, 0.609f, 0.652f,
            0.696f, 0.739f, 0.783f, 0.826f, 0.870f, 0.913f, 0.957f, 1.0f
        ),
        sunrise = 0.290f,
        sunset = 0.80f,
        current = 0.913f
    )
}