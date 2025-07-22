package com.example.weatherappcompose.ui.theme.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Vertices
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherappcompose.R
import com.example.weatherappcompose.open_meteo.Current
import com.example.weatherappcompose.wmo.getWeatherCondition
import kotlin.math.roundToInt

@Composable
fun CurrentWeatherLoading(
    location: String
) {

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = location,
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
}

@Composable
fun CurrentWeatherExpanded(
    isHome: Boolean,
    location: String,
    temperature: Int,
    description: String,
    minTemperature: Int,
    maxTemperature: Int,
    appBarCollapsed: Boolean,
    appBarCollapsed1: Boolean,
    appBarCollapsed2: Boolean
) {
    val alpha by animateFloatAsState(
        targetValue = if (appBarCollapsed) 1f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "expanded_weather_alpha"
    )
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = location,
            fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 40.sp,
            color = Color.White
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

                Row(
                    modifier = Modifier.alpha(alpha),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "$temperature",
                        fontWeight = FontWeight.Thin,
                        fontFamily = FontFamily(Font(R.font.sf_pro_display_thin)),
                        fontSize = 100.sp,
                        modifier = Modifier,
                        color = Color.White
                    )
                    Text(
                        text = "°",
                        fontWeight = FontWeight.Thin,
                        fontFamily = FontFamily(Font(R.font.sf_pro_display_thin)),
                        fontSize = 100.sp,
                        modifier = Modifier
                            .height(140.dp),
                        color = Color.White
                    )
                }

            AnimatedVisibility(
                visible = appBarCollapsed2,
                enter = fadeIn(animationSpec = tween()),
                exit = fadeOut(animationSpec = tween())
            ) {
                Text(
                    modifier = Modifier.alpha(0.6f),
                    text = description,
                    fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                    fontSize = 20.sp,
                    color = Color.White
                )
            }

            AnimatedVisibility(
                visible = appBarCollapsed1,
                enter = fadeIn(animationSpec = tween()),
                exit = fadeOut(animationSpec = tween())
            ) {
                Text(
                    text = "L:${minTemperature}° H:${maxTemperature}°",
                    fontFamily = FontFamily(Font(R.font.sf_pro_display_medium)),
                    fontSize = 20.sp,
                    color = Color.White
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
            )
        }
    }
}

@Composable
fun CurrentWeatherCollapsed(
    location: String,
    description: String,
    temperature: Int,
    visible: Boolean,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {

        Text(
            text = location,
            fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 40.sp,
            color = Color.White
        )

        Row {
            Text(
                modifier = Modifier.alpha(0.6f),
                text = "${temperature}°",
                fontWeight = FontWeight.Thin,
                fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                fontSize = 20.sp,
                color = Color.White
            )
            Text(
                modifier = Modifier.alpha(0.6f),
                text="  |   ",
                fontWeight = FontWeight.Thin,
                fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                fontSize = 20.sp,
                color = Color.White
            )
            Text(
                modifier = Modifier.alpha(0.6f),
                text = description,
                fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                fontSize = 20.sp,
                color = Color.White
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
        )
    }
}

@Preview
@Composable
fun CurrentWeatherPreview() {
    CurrentWeatherExpanded(
        false,
        "Mustang",
        12,
        "Partly Cloudy",
        6,
        16,
        true,
        true,
        true
    )
}

@Preview
@Composable
fun CurrentWeatherLoadingPreview() {
    CurrentWeatherLoading(

        "Mustang"
    )
}

@Preview
@Composable
fun CurrentWeatherCollapsedPreview(){
    CurrentWeatherCollapsed(
        "Mustang",
        "Light Snow",
        12,
        true
    )
}

