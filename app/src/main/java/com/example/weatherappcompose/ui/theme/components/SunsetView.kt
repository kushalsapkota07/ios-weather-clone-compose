import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/**
 * Represents the data needed for the sunset UI.
 * @param sunrise The time of sunrise (e.g., "06:00:00").
 * @param sunset The time of sunset (e.g., "18:00:00").
 * @param currentTime The current time (e.g., "12:00:00"). This will drive the animation.
 * @param sunshineDurationSeconds The number of seconds of actual sunshine.
 * @param daylightDurationSeconds The number of seconds of daylight.
 */
data class SunData(
    val sunrise: String,
    val sunset: String,
    val currentTime: String,
    val sunshineDurationSeconds: Long,
    val daylightDurationSeconds: Long
)

/**
 * A composable function that displays a sunset UI similar to the iOS weather app.
 * It visualizes the sun's arc, sky color changes, and displays sunrise/sunset times.
 *
 * @param sunData The data containing sunrise, sunset, current time, and durations.
 */
@Composable
fun SunsetTracker(sunData: SunData) {
    // Parse times for calculations
    val sunriseTime = remember(sunData.sunrise) { LocalTime.parse(sunData.sunrise) }
    val sunsetTime = remember(sunData.sunset) { LocalTime.parse(sunData.sunset) }
    val currentTime = remember(sunData.currentTime) { LocalTime.parse(sunData.currentTime) }

    // Calculate total daylight duration in minutes
    val totalDaylightMinutes = remember(sunriseTime, sunsetTime) {
        ChronoUnit.MINUTES.between(sunriseTime, sunsetTime).toFloat()
    }

    // Calculate current progress of the day between sunrise and sunset (0.0 to 1.0)
    val progress = remember(currentTime, sunriseTime, sunsetTime) {
        if (currentTime.isBefore(sunriseTime) || currentTime.isAfter(sunsetTime)) {
            // If outside daylight hours, sun is "down"
            if (currentTime.isAfter(sunsetTime)) 1f else 0f
        } else {
            val elapsedMinutes = ChronoUnit.MINUTES.between(sunriseTime, currentTime).toFloat()
            (elapsedMinutes / totalDaylightMinutes).coerceIn(0f, 1f)
        }
    }

    // Animate the progress for smooth transitions
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
        label = "sunProgressAnimation"
    )

    // Define a static blue background for the card, similar to iOS
    val cardBackgroundColor = Color(0xFF2A84D2) // A shade of blue similar to the iOS app

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp) // Adjusted height to match iOS example
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp), // Rounded corners for the card
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top section: "SUNSET" with icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = " \uD83C\uDF05 SUNSET", // Sun icon emoji
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Middle section: Large Sunset Time
            Text(
                text = LocalTime.parse(sunData.sunset)
                    .format(DateTimeFormatter.ofPattern("hh:mm a")),
                color = Color.White,
                fontSize = 48.sp, // Larger font size for prominence
                fontWeight = FontWeight.Light, // Lighter weight for modern look
                modifier = Modifier.align(Alignment.Start) // Align to start (left)
            )

            // Canvas for drawing the sun's arc and sun
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Takes available space
            ) {
                val width = size.width
                val height = size.height
                val centerX = width / 2f
                val horizonY =
                    height * 0.7f // Adjusted horizon to be lower, giving more space for the arc

                // Draw the horizon line
                drawLine(
                    color = Color.White.copy(alpha = 0.7f),
                    start = Offset(0f, horizonY),
                    end = Offset(width, horizonY),
                    strokeWidth = 2.dp.toPx()
                )

                // Define the control points for a quadratic bezier curve to mimic the iOS arc
                // This creates a smoother, more natural looking arc that dips below the horizon
                val startPoint = Offset(0f, horizonY)
                val endPoint = Offset(width, horizonY)
                // Adjust this multiplier to change the height/depth of the arc.
                // A smaller multiplier (e.g., 0.5f) will make the arc flatter.
                // A larger multiplier (e.g., 0.9f) will make the arc taller/deeper.
                val controlPoint = Offset(centerX, horizonY - height * 1f)

                val path = Path().apply {
                    moveTo(startPoint.x, startPoint.y)
                    quadraticBezierTo(
                        controlPoint.x, controlPoint.y,
                        endPoint.x, endPoint.y
                    )
                }
                drawPath(
                    path = path,
                    color = Color.White.copy(alpha = 0.5f),
                    style = Stroke(width = 2.dp.toPx())
                )

                // Calculate sun position along the arc using linear interpolation for simplicity
                // For a quadratic bezier, calculating the exact point along the curve at a given 't' (progress)
                // is done with a specific formula: (1-t)^2*P0 + 2(1-t)t*P1 + t^2*P2
                val t = animatedProgress
                val sunX =
                    (1 - t) * (1 - t) * startPoint.x + 2 * (1 - t) * t * controlPoint.x + t * t * endPoint.x
                val sunY =
                    (1 - t) * (1 - t) * startPoint.y + 2 * (1 - t) * t * controlPoint.y + t * t * endPoint.y

                // Draw the sun (white dot as in iOS)
                drawCircle(
                    color = Color.White, // Changed sun color to white
                    radius = 8.dp.toPx(), // Smaller radius for the dot
                    center = Offset(sunX, sunY)
                )
            }

            // Bottom section: Sunrise Time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End, // Align to end (right)
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sunrise: ${
                        LocalTime.parse(sunData.sunrise)
                            .format(DateTimeFormatter.ofPattern("hh:mm a"))
                    }",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSunsetTracker() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray) // Background for the preview to show the card clearly
                .wrapContentSize(Alignment.Center)
        ) {
            SunsetTracker(
                sunData = SunData(
                    sunrise = "07:18:00",
                    sunset = "18:03:00",
                    currentTime = "08:30:00", // Example: morning
                    sunshineDurationSeconds = 30000,
                    daylightDurationSeconds = 43200
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            SunsetTracker(
                sunData = SunData(
                    sunrise = "07:18:00",
                    sunset = "18:03:00",
                    currentTime = "14:00:00", // Example: afternoon
                    sunshineDurationSeconds = 30000,
                    daylightDurationSeconds = 43200
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            SunsetTracker(
                sunData = SunData(
                    sunrise = "07:18:00",
                    sunset = "18:03:00",
                    currentTime = "17:45:00", // Example: near sunset
                    sunshineDurationSeconds = 30000,
                    daylightDurationSeconds = 43200
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            SunsetTracker(
                sunData = SunData(
                    sunrise = "07:18:00",
                    sunset = "18:03:00",
                    currentTime = "05:00:00", // Example: before sunrise
                    sunshineDurationSeconds = 30000,
                    daylightDurationSeconds = 43200
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            SunsetTracker(
                sunData = SunData(
                    sunrise = "07:18:00",
                    sunset = "18:03:00",
                    currentTime = "19:00:00", // Example: after sunset
                    sunshineDurationSeconds = 30000,
                    daylightDurationSeconds = 43200
                )
            )
        }
    }
}
