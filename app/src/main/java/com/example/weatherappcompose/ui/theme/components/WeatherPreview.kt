package com.example.weatherappcompose.ui.theme.components

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherappcompose.R
import com.example.weatherappcompose.db.SimpleLocation
import com.example.weatherappcompose.db.WeatherSummary
import com.example.weatherappcompose.db.toCurrentValues
import com.example.weatherappcompose.ui.theme.screens.Route
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.launch

@SuppressLint("ResourceType")
@Composable
fun WeatherPreview(
    modifier: Modifier,
    simpleLocation: SimpleLocation,
    weatherSummary: List<String>,
    onRemove: () -> Unit,
    onNavigateToWeatherScreen: (Route.WeatherRoute) -> Unit,
    imageResource: Int
) {
    val imageHaze = rememberHazeState()

    val swipeToDismissBoxState = rememberSwipeToDismissBoxState(
        confirmValueChange = { state ->
            if (state==SwipeToDismissBoxValue.EndToStart){
                onRemove()
                true
            } else{
                false
            }
        }
    )

    AnimatedVisibility(
        visible = true
    ) {
        SwipeToDismissBox(
            state = swipeToDismissBoxState,
            modifier = modifier.fillMaxWidth().wrapContentHeight()
                .animateContentSize(animationSpec = tween()),
            backgroundContent = {
            Box(
                Modifier
                    .fillMaxSize().padding(horizontal = 16.dp, vertical = 4.dp)
            ){
                Icon(
                    modifier = Modifier.align(Alignment.CenterEnd)
                        .clip(CircleShape)
                        .background(Color.Red)
                        .padding(10.dp),
                    painter = painterResource(R.drawable.outline_delete_24),
                    contentDescription = "Delete Location"
                )
            }
            },
            enableDismissFromStartToEnd = false,
            enableDismissFromEndToStart = true,
            gesturesEnabled = true
        ) {
            Box(
                modifier = modifier
                    .clickable {
                        onNavigateToWeatherScreen(
                            Route.WeatherRoute(
                                simpleLocation.name,
                                simpleLocation.lat,
                                simpleLocation.lon,
                                false
                            )
                        )
                    }
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .clip(RoundedCornerShape(12.dp))

            ) {
                if (imageResource != 0) {
                    Image(
                        painter = painterResource(id = imageResource),
                        contentDescription = "Weather Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.matchParentSize().hazeSource(imageHaze)
                    )
                } else {
                    Image(
                        painter = painterResource(integerResource(imageResource)),
                        contentDescription = "Weather Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.matchParentSize().hazeSource(imageHaze)
                    )
                }
                Row(
                    modifier = Modifier
                        .wrapContentSize()
                        .background(Color.Transparent)
                        .hazeEffect(imageHaze)
                        .padding(6.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .wrapContentHeight()
                            .padding(10.dp)
                            .weight(2f),
                        verticalArrangement = Arrangement.SpaceAround,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = simpleLocation.name,
                            fontSize = 18.sp,
                            fontFamily = FontFamily(
                                Font(
                                    R.font.sf_pro_display_bold
                                )
                            ),
                            color = Color.White
                        )
                        Text(
                            text = simpleLocation.time,
                            fontSize = 12.sp,
                            fontFamily = FontFamily(
                                Font(
                                    R.font.sf_pro_display_medium
                                )
                            ),
                            color = Color.White
                        )
                        Icon(
                            painter = painterResource(R.drawable.baseline_access_time_24),
                            tint = Color.White,
                            contentDescription = "Warning Sign"
                        )
                        Text(
                            text = weatherSummary[0],
                            fontSize = 12.sp,
                            fontFamily = FontFamily(
                                Font(
                                    R.font.sf_pro_display_medium
                                )
                            ),
                            color = Color.White
                        )
                    }
                    Column(
                        modifier = Modifier
                            .wrapContentHeight()
                            .padding(10.dp)
                            .weight(1f),
                        verticalArrangement = Arrangement.SpaceAround,
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = weatherSummary[1],
                            fontSize = 42.sp,
                            fontFamily = FontFamily(
                                Font(
                                    R.font.sf_pro_display_regular
                                )
                            ),
                            color = Color.White
                        )
                        Text(
                            text = "L:${weatherSummary[2]}° H:${weatherSummary[3]}°",
                            fontFamily = FontFamily(
                                Font(
                                    R.font.sf_pro_display_medium
                                )
                            ),
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}


@Preview(
    showBackground = true,
    showSystemUi = false
)
@Composable
fun WeatherPreviewPreview() {
    WeatherPreview(
        Modifier, simpleLocation = SimpleLocation(), listOf("Sunny", "12", "6", "17"),
        onNavigateToWeatherScreen = {},
        imageResource = R.raw.snow_night,
        onRemove = {}
    )
}