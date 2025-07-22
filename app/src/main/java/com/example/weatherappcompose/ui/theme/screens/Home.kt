package com.example.weatherappcompose.ui.theme.screens

import android.annotation.SuppressLint
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.unit.dp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.weatherappcompose.R
import com.example.weatherappcompose.db.SimpleLocation
import com.example.weatherappcompose.db.toCurrentValues
import com.example.weatherappcompose.repository.getTimeAgo
import com.example.weatherappcompose.ui.theme.components.AppBarHeader
import com.example.weatherappcompose.ui.theme.components.CollapsedAppBar
import com.example.weatherappcompose.ui.theme.components.SearchBarMinimal
import com.example.weatherappcompose.ui.theme.components.WeatherPreview
import com.example.weatherappcompose.ui.theme.states.DropDownItem
import com.example.weatherappcompose.ui.theme.states.itemList
import com.example.weatherappcompose.viewmodel.ConnectivityViewModel
import com.example.weatherappcompose.viewmodel.LocationViewModel
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import java.time.LocalDateTime

@SuppressLint("ResourceType")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    connectivityViewModel: ConnectivityViewModel,
    locationViewModel: LocationViewModel,
    onNavigateToBottomSheetWeatherScreen: (Route.BottomSheetWeatherRoute) -> Unit,
    onNavigateToWeather: (Route.WeatherRoute) -> Unit,
    onNavigateToSearchScreen: (Route.SearchRoute) -> Unit
){

    val weatherSummary by locationViewModel.weatherSummaries.collectAsState()
    val savedLocations by locationViewModel.savedLocations.collectAsState()
    val lastUpdated by locationViewModel.lastUpdated.collectAsState()

    val isConnected by connectivityViewModel.isConnected.collectAsState()

    println("Network Connection: $isConnected")

    val expandedAppBarHeight = 56.dp
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        snapAnimationSpec = tween()
    )
    val scrollState = scrollBehavior.state
    val appBarExpanded by remember {
        // app bar expanded state
        // collapsedFraction 1f = collapsed
        // collapsedFraction 0f = expanded
        derivedStateOf { scrollState.collapsedFraction < 0.9f }
    }
    val headerTranslation = (expandedAppBarHeight*2)

    val hazeState = rememberHazeState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1F303E))
            .hazeSource(state = hazeState)
    ) {
    }
        Scaffold(
            modifier = Modifier.background(Color.Transparent),
            containerColor = Color.Transparent,
            topBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                        .hazeEffect(state = hazeState)
                ) {
                    CollapsedAppBar(
                        visible = !appBarExpanded,
                        state = hazeState
                    )
                    TopAppBar(
                        modifier = Modifier.padding(),
                        title = {
                            AppBarHeader(
                                modifier = Modifier.graphicsLayer {
                                    translationY =
                                        scrollState.collapsedFraction / headerTranslation.toPx()
                                },
                                visible = appBarExpanded,
                                state = hazeState
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            // set background color for expanded state to transparent
                            containerColor = Color.Transparent,
                            // set background color for collapsed state to transparent
                            scrolledContainerColor = Color.Transparent
                        ),
                        expandedHeight = expandedAppBarHeight,
                        // remove extra padding on top (from status bar)
                        windowInsets = WindowInsets(0, 0, 0, 0),
                        scrollBehavior = scrollBehavior
                    )
                }
            },

        ) { paddingValues ->
                LazyColumn(
                    modifier = Modifier
                        .padding(paddingValues)
                        .nestedScroll(scrollBehavior.nestedScrollConnection)
                ) {
                    stickyHeader {

                        if (!appBarExpanded) {
                            SearchBarMinimal(
                                modifier = Modifier,
                                hazeState = hazeState
                            ) {
                                onNavigateToSearchScreen(
                                    Route.SearchRoute("")
                                )
                            }
                        } else {
                            SearchBarMinimal(
                                modifier = Modifier,
                                hazeState = hazeState

                            ) {
                                onNavigateToSearchScreen(
                                    Route.SearchRoute("")
                                )
                            }
                        }
                    }
                    item {
                        if (!isConnected) {
                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "No internet connection",
                                    fontSize = 16.sp,
                                    color = Color.White,
                                    textAlign = TextAlign.Center,
                                    fontFamily = FontFamily(Font(R.font.sf_pro_display_regular))
                                )
                                if (lastUpdated != null) {
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = "Last Updated: ${getTimeAgo(lastUpdated!!)}",
                                        fontSize = 16.sp,
                                        color = Color.Gray,
                                        textAlign = TextAlign.Center,
                                        fontFamily = FontFamily(Font(R.font.sf_pro_display_regular))
                                    )
                                }

                            }
                        } else{
                            locationViewModel.refreshWeatherData()
                        }
                    }
                    items(
                        count = savedLocations.size,
                        key = { listOf( savedLocations[it].lat , savedLocations[it].lon) }
                    ) {
                        println("Inside Items")
                        val simpleLocation = savedLocations[it]

                        WeatherPreview(
                            modifier = Modifier.hazeSource(state = hazeState),
                            simpleLocation = simpleLocation,
                            onNavigateToWeatherScreen = onNavigateToWeather,
                            weatherSummary = weatherSummary[it].toCurrentValues(),
                            imageResource = weatherSummary[it].toCurrentValues().last().toInt(),
                            onRemove = {
                                locationViewModel.deleteLocationData(simpleLocation)
                            }
                        )
                    }
                }
            }

}

@Composable
fun DropDownList(
    items: List<DropDownItem>,
    onItemClick: (DropDownItem)->Unit,
    expanded: Boolean,
    onDismissRequest: () -> Unit
){
    DropdownMenu(
        modifier = Modifier
            .fillMaxWidth(.6f)
            .padding(0.dp)
        ,
        expanded = expanded,
        onDismissRequest = {
            onDismissRequest()
        },

        ) {
        itemList.forEachIndexed { index, item ->
            Column(
                modifier = Modifier
                    .clickable { onItemClick(item) }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier
                            .alpha(0f)
                            .weight(1f),
                        painter = painterResource(
                            id = R.drawable.baseline_check_24
                        ),
                        contentDescription = "Checked"
                    )
                    Text(
                        modifier = Modifier.weight(5f),
                        text = item.itemName
                    )
                    Icon(
                        modifier = Modifier.weight(1f),
                        painter = painterResource(id = item.icon),
                        contentDescription = item.itemName
                    )
                }
                when (index) {
                    0, 2 -> {
                        HorizontalDivider(
                            modifier = Modifier,
                            thickness = 0.5.dp,
                            color = Color.White
                        )
                    }

                    1, 3, 4 -> {
                        HorizontalDivider(
                            modifier = Modifier,
                            thickness = 8.dp, // Thin line like iOS
                        )
                    }
                }
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true, showSystemUi = false)
@Composable
fun HomeScreenPreview(){
}