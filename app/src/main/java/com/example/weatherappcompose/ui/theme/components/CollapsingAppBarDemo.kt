package com.example.weatherappcompose.ui.theme.components


import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherappcompose.R
import com.example.weatherappcompose.db.SimpleLocation
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.rememberHazeState
import okhttp3.internal.wait

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollapsingAppBarDemo(
    modifier: Modifier = Modifier
){

    val expandedAppBarHeight = 56.dp
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        snapAnimationSpec = tween()
    )
    val scrollState = scrollBehavior.state
    val appBarExpanded by remember {
        // app bar expanded state
        // collapsedFraction 1f = collapsed
        // collapsedFraction 0f = expanded
        derivedStateOf { scrollState.collapsedFraction < 0.9f }
    }

    val appBarCollapsed1 by remember {
        // app bar expanded state
        // collapsedFraction 1f = collapsed
        // collapsedFraction 0f = expanded
        derivedStateOf { scrollState.collapsedFraction < 0.3f }
    }
    val appBarCollapsed2 by remember {
        // app bar expanded state
        // collapsedFraction 1f = collapsed
        // collapsedFraction 0f = expanded
        derivedStateOf { scrollState.collapsedFraction < 0.4f }
    }
    val appBarCollapsed by remember {
        // app bar expanded state
        // collapsedFraction 1f = collapsed
        // collapsedFraction 0f = expanded
        derivedStateOf { scrollState.collapsedFraction < 1f }
    }
    val headerTranslation = (expandedAppBarHeight)
    val listState = rememberLazyListState()

    val allowExpand = remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0 &&
                    listState.firstVisibleItemScrollOffset == 0
        }
    }

    val controlledNestedScroll = remember(allowExpand.value) {
        if (allowExpand.value) {
            scrollBehavior.nestedScrollConnection
        } else {
            object : NestedScrollConnection {
                // Prevent top bar from expanding
                override fun onPreScroll(
                    available: Offset,
                    source: NestedScrollSource
                ): Offset = Offset.Zero
            }
        }
    }



    Scaffold(
        modifier = modifier.fillMaxSize().nestedScroll(controlledNestedScroll),
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                CollapsedAppBar(
                    visible = !appBarExpanded,
                    state = rememberHazeState()
                )
                TopAppBar(
                    modifier = Modifier,
                    title = {
                        AppBarHeader(
                            modifier = Modifier.graphicsLayer {
                                translationY = scrollState.collapsedFraction / headerTranslation.toPx()
                            }
                            ,
                            visible = appBarExpanded,
                            state = rememberHazeState()
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
                    windowInsets = WindowInsets(0,0,0,0),
                    scrollBehavior = scrollBehavior
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            PhotoGrid(
                modifier = Modifier,
                items = List(15){"Sample Text"},
                state = listState,
                appBarExpanded = appBarExpanded,
                appBarCollapsed = appBarCollapsed,
                appBarCollapsed1 = appBarCollapsed1,
                appBarCollapsed2 = appBarCollapsed2
            )

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoGrid(
    modifier: Modifier = Modifier,
    state: LazyListState,
    items: List<String>,
    appBarExpanded: Boolean,
    appBarCollapsed: Boolean,
    appBarCollapsed1: Boolean,
    appBarCollapsed2: Boolean
) {
    val colors = TopAppBarDefaults.topAppBarColors(
        containerColor = Color.Transparent,
        scrolledContainerColor = Color.Transparent
    )
    val searchBarCollapsedColor = colors.scrolledContainerColor

        if (!appBarExpanded){
            CurrentWeatherCollapsed(
                location = "London",
                description = "Partly Cloudy",
                temperature = 12,
                visible = appBarExpanded,
            )
        } else{
            CurrentWeatherExpanded(
                isHome = true,
                location = "London",
                temperature = 12,
                description = "Partly Cloudy",
                minTemperature = 6,
                maxTemperature = 16,
                appBarCollapsed = appBarCollapsed,
                appBarCollapsed1 = appBarCollapsed1,
                appBarCollapsed2 = appBarCollapsed2
            )
        }

    LazyColumn(
        state = state,
        modifier = modifier
    ) {
        items(items.size){
            WeatherPreview(
                modifier = Modifier,
                simpleLocation = SimpleLocation(
                    "Location",
                    0.0,
                    0.0,
                    1,
                    "",
                    ""
                ),
                weatherSummary = listOf("Sunny", "12", "6", "17"),
                imageResource = R.raw.heavy_rain_night,
                onNavigateToWeatherScreen = {},
                onRemove = {}
            )
        }
    }

}

@Composable
fun AppBarHeader(
    modifier: Modifier = Modifier,
    visible: Boolean,
    state: HazeState
) {
    // animate fadeIn fadeOut the expanded app bar header
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween()),
        exit = fadeOut(animationSpec = tween())
    ) {
        Column(
            // only add end padding
            // because the TopAppBar already has start padding
            modifier = modifier.hazeEffect(state = state),
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                    ,
                    text = "Weather",
                    fontFamily = FontFamily(Font(R.font.sf_pro_display_bold)),
                    fontSize = 32.sp,
                    color = Color.White
                )

            }

            // extra space at the bottom of the header
            // use spacer to prevent content being clipped
            Spacer(modifier = Modifier.height(8.dp))
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollapsedAppBar(
    modifier: Modifier = Modifier,
    visible: Boolean,
    state: HazeState
) {
    TopAppBar(
        colors = TopAppBarDefaults.largeTopAppBarColors(Color.Transparent),
        modifier = modifier.fillMaxWidth().hazeEffect(state = state),
        title = {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween()),
                exit = fadeOut(animationSpec = tween())
            ){
                Row(
                    modifier = Modifier.background(Color.Transparent).fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = "Weather",
                        fontFamily = FontFamily(Font(R.font.sf_pro_display_bold)),
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }

        }
    )
}

@Preview(showSystemUi = true)
@Composable
fun CollapsingAppBarDemoPreview(){
    CollapsingAppBarDemo()
}