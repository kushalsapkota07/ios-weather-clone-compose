package com.example.weatherappcompose.ui.theme.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherappcompose.R
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.rememberHazeState
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Compass(
    modifier: Modifier,
    wind:Int,
    gusts: Int,
    direction: Int,
    hazeState: HazeState
){
    val textColor = Color.White
    Box(
        modifier  = modifier
            .height(120.dp)
            .clip(RoundedCornerShape(0.dp, 0.dp, 16.dp, 16.dp))
            .hazeEffect(state = hazeState)
    ) {
        Row(
            modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(2f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier,
                        text = "Wind",
                        fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                        fontSize = 16.sp,
                        color = textColor
                    )
                    Text(
                        text = "$wind",
                        color = textColor,
                        fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                        fontSize = 16.sp
                    )
                }
                HorizontalDivider(Modifier.padding(horizontal = 8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier,
                        text = "Gusts",
                        fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    Text(
                        text = "$gusts",
                        color = textColor,
                        fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                        fontSize = 16.sp
                    )
                }
                HorizontalDivider(Modifier.padding(horizontal = 8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier,
                        text = "Direction",
                        fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    Text(
                        text = "$direction °",
                        color = textColor,
                        fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                        fontSize = 16.sp
                    )
                }
            }

            Box(
                modifier = Modifier.weight(1f)
            ) {
                Canvas(
                    modifier = Modifier.matchParentSize()
                ) {
                    val compassRadius = size.width / 2f
                    val width = size.width
                    val height = size.height

                    val circleCenter = Offset(x = width / 2f, y = height / 2f)

                    val lineLength = compassRadius * 0.2f
                    val gray = Color(0xCC888888)

                    for (i in 0 until 120) {
                        val angleInDegrees = i * 360f / 120
                        val angleInRad = angleInDegrees * PI / 180f + PI / 2f
                        val lineThickness = 1f
                        val lineColor = if (i % 10 == 0) Color.White else gray

                        val start = Offset(
                            x = (compassRadius * cos(angleInRad) + circleCenter.x).toFloat(),
                            y = (compassRadius * sin(angleInRad) + circleCenter.y).toFloat()
                        )
                        val end = Offset(
                            x = (compassRadius * cos(angleInRad) + circleCenter.x).toFloat(),
                            y = (compassRadius * sin(angleInRad) + lineLength + circleCenter.y).toFloat()
                        )

                        scale(scaleX = .8f, scaleY = .8f) {
                            rotate(
                                angleInDegrees,
                                pivot = start
                            ) {
                                when {
                                    (i % 30 == 0 || i % 30 == 1 || i % 30 == 2) -> drawLine(
                                        color = Color.Transparent,
                                        start = start,
                                        end = end,
                                        strokeWidth = lineThickness.dp.toPx()
                                    )

                                    (i == 29 || i == 28 || i == 59 || i == 58 || i == 88 || i == 89 || i == 118 || i == 119) -> {
                                        drawLine(
                                            color = Color.Transparent,
                                            start = start,
                                            end = end,
                                            strokeWidth = lineThickness.dp.toPx()
                                        )
                                    }

                                    else -> drawLine(
                                        color = lineColor,
                                        start = start,
                                        end = end,
                                        strokeWidth = lineThickness.dp.toPx()
                                    )
                                }
                            }
                        }
                    }
                    val compassCenter = center
                    val radius = size.width / 2.5f // slightly smaller than outer radius
                    val textPaint = Paint().apply {
                        color = android.graphics.Color.WHITE
                        textSize = 32f
                        textAlign = Paint.Align.CENTER
                        isAntiAlias = true
                    }
                    val directions = listOf("N", "E", "S", "W")
                    val directionAngles = listOf(270f, 0f, 90f, 180f) // degrees

                    for (i in directions.indices) {
                        val angle = Math.toRadians(directionAngles[i].toDouble())
                        val x = compassCenter.x + cos(angle) * radius
                        val y =
                            compassCenter.y + sin(angle) * radius + textPaint.textSize / 3 // adjust Y for centering

                        drawContext.canvas.nativeCanvas.drawText(
                            directions[i],
                            x.toFloat(),
                            y.toFloat(),
                            textPaint
                        )
                    }

                    val start = Offset(0f, size.height / 2)
                    val end = Offset(size.width, size.height / 2)

                    // Draw arrow line
                    val path = Path().apply {
                        moveTo(start.x, start.y)
                        lineTo(end.x, end.y)
                    }

                    // Draw arrowhead at the end
                    val arrowHeadLength = size.width / 2f * 0.2f
                    val angle = Math.toRadians(30.0) // 30 degree angle
                    val dir = (start - end).normalize()

                    val leftWing = Offset(
                        (end.x + arrowHeadLength * cos(dir.angle() + angle)).toFloat(),
                        (end.y + arrowHeadLength * sin(dir.angle() + angle)).toFloat()
                    )
                    val rightWing = Offset(
                        (end.x + arrowHeadLength * cos(dir.angle() - angle)).toFloat(),
                        (end.y + arrowHeadLength * sin(dir.angle() - angle)).toFloat()
                    )

                    val arrowHeadPath = Path().apply {
                        moveTo(end.x, end.y)
                        lineTo(leftWing.x, leftWing.y)
                        moveTo(end.x, end.y)
                        lineTo(rightWing.x, rightWing.y)
                    }
                    withTransform(
                        {
                            rotate(direction.toFloat())
                            scale(scaleX = 0.8f, scaleY = 0.8f)
                        }
                    ) {
                        drawPath(
                            path = path,
                            color = Color.White,
                            style = Stroke(width = 10f)
                        )

                        // Draw circle at the start
                        drawCircle(
                            color = Color.White,
                            radius = compassRadius / 2f * 0.2f,
                            center = start
                        )

                        drawCircle(
                            color = Color.Gray,
                            radius = compassRadius / 2f,
                            center = center
                        )
                        drawPath(
                            path = arrowHeadPath,
                            color = Color.White,
                            style = Stroke(width = 10f)
                        )

                    }
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val color = Color.White
                    Text(
                        fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                        text = "$wind", color = color,
                        )
                    Text(
                        fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                        text = "km/h",
                        color = color
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = false, showBackground = true)
@Composable
fun WindCompassPreview(
){
    Compass(
        modifier = Modifier,
        wind = 12,
        gusts = 5,
        direction = 120,
        hazeState = rememberHazeState()
    )

//    Arrow(Modifier.fillMaxSize())
}

fun Offset.normalize(): Offset {
    val length = getDistance()
    return if (length != 0f) Offset(x / length, y / length) else Offset.Zero
}

fun Offset.angle(): Double {
    return kotlin.math.atan2(y, x).toDouble()
}
