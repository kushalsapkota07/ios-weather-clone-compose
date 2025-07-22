package com.example.weatherappcompose.ui.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState

val modifier = Modifier
    .fillMaxWidth(.4f)
    .height(120.dp)
    .clip(RoundedCornerShape(0.dp, 0.dp, 16.dp, 16.dp))
//    .background(Color(0xFF1C1C1E).copy(alpha = 0.6f))


@Composable
fun Group1(apparentTemperature: Int, uvIndex: Int, hazeState: HazeState) {

    Row(
        modifier = Modifier.fillMaxWidth(.9f),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        FeelsLike(modifier = modifier.weight(1f).hazeEffect(state = hazeState), apparentTemperature = apparentTemperature)
        UvIndex(modifier = modifier.weight(1f).hazeEffect(state = hazeState), uvIndex = uvIndex)
    }
}

@Composable
fun Group2(visibility: Int, precipitation: List<Int>, hazeState: HazeState) {
    Row(
        modifier = Modifier.fillMaxWidth(.9f),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Visibility(modifier.weight(1f).hazeEffect(state = hazeState), visibility = visibility)
        Precipitation(modifier.weight(1f).hazeEffect(state = hazeState), precipitation = precipitation)
    }
}

@Composable
fun Group3(visibility: Int, hours: List<Long>, sunrise: Long, sunset: Long, current: Long, hazeState: HazeState) {
    Row(
        modifier = Modifier.fillMaxWidth(.9f),
        horizontalArrangement = Arrangement.spacedBy(10.dp)

    ) {
        Visibility(modifier.weight(1f).hazeEffect(state = hazeState), visibility = visibility)
        SunArcView(
            modifier = modifier.weight(1f).hazeEffect(state = hazeState),
            hours = hours,
            sunrise = sunrise,
            sunset = sunset,
            current = current
        )
    }
}

