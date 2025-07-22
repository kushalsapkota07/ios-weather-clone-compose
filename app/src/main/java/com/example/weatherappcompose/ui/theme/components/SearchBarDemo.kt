package com.example.weatherappcompose.ui.theme.components

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.delete
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherappcompose.R
import com.example.weatherappcompose.ui.theme.screens.Route
import com.example.weatherappcompose.viewmodel.ConnectivityViewModel
import com.example.weatherappcompose.viewmodel.LocationViewModel
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState


@Composable
fun SearchBarSample(
    connectivityViewModel: ConnectivityViewModel,
    locationViewModel: LocationViewModel,
    modifier: Modifier,
    onNavigateToBottomSheetWeatherScreen: (Route.BottomSheetWeatherRoute) -> Unit,
    onNavigateToSearchScreen: (Route.SearchRoute) -> Unit
) {
    val textFieldState = rememberTextFieldState()

    var expanded by remember {
        mutableStateOf(false)
    }
    val interactionSource = remember {
        MutableInteractionSource()
    }
    var query by remember {
        mutableStateOf(TextFieldValue(
            text = "",
            selection = TextRange.Zero
            ))
    }
    

        Scaffold(
            containerColor = Color(0xFF1F303E)
        ) { paddingValues ->


            //Autocomplete lists
            Column(
                modifier = Modifier
                    .background(Color.Transparent)
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(paddingValues)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = {
                            expanded = false
                        }

                    )
            ) {

                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(
                            color = Color.Gray.copy(0.2f),
                            shape = RoundedCornerShape(6.dp)
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Icon(
                        modifier = Modifier
                            .padding(start = 8.dp, end = 0.dp),
                        imageVector = Icons.Rounded.Search,
                        contentDescription = "Search Icon",
                        tint = Color.Gray
                    )

                    BasicTextField(
                        value = query,
                        onValueChange = {
                            locationViewModel.searchPlaces(it.text)
                            query = it
                            expanded = true
                        },

                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(start = 0.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
                        cursorBrush = SolidColor(Color.White),
                        textStyle = TextStyle(
                            color = Color.White,
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            Row(
                                modifier = Modifier
                            ) {
                                if (query.text.isEmpty()) {
                                    Text(
                                        modifier = Modifier,
                                        color = Color.Gray,
                                        text = stringResource(R.string.search_bar_hint),
                                        fontSize = 16.sp
                                    )
                                }
                                innerTextField()
                            }

                        },
                    )

                    AnimatedVisibility(
                        visible = expanded,
                        enter = fadeIn(animationSpec = tween()),
                        exit = fadeOut(animationSpec = tween())
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Clear,
                            contentDescription = "Delete Text Icon",
                            tint = Color.Gray,
                            modifier = Modifier
                                .padding(4.dp)
                                .clickable {
                                    textFieldState.edit {
                                        delete(0, length)
                                    }
                                }
                        )
                    }
                }

                AnimatedVisibility(
                    locationViewModel.locationAutoFill.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth(),
                    enter = fadeIn()
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        items(locationViewModel.locationAutoFill.size){

                            val location = locationViewModel.locationAutoFill[it]

                            Text(
                                text = locationViewModel.locationAutoFill[it].address,
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .fillMaxWidth()
                                    .clickable(
                                        onClick = {
                                            locationViewModel.getCoordinates(location){ latLng ->
                                                onNavigateToBottomSheetWeatherScreen(
                                                    Route.BottomSheetWeatherRoute(
                                                        location.address,
                                                        latLng.latitude,
                                                        latLng.longitude
                                                    )
                                                )
                                            }
                                        }
                                    ),
                                color = Color.White

                            )
                        }
                    }
                }

            }

        }
    }


@Composable
fun SearchBarMinimal(
    modifier: Modifier,
    text: String = "",
    hazeState: HazeState,
    onClick: () -> Unit,

) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val focusState = remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .background(Color.Transparent)
            .fillMaxWidth()
            .hazeEffect(state = hazeState)
    ) {
        Row(
            modifier = Modifier
                .clickable {
                    onClick()
                }
                .hazeEffect(state = hazeState)
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .background(
                    color = Color.Gray.copy(0.4f),
                    shape = RoundedCornerShape(6.dp)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Icon(
                modifier = Modifier
                    .padding(start = 8.dp, end = 0.dp),
                imageVector = Icons.Rounded.Search,
                contentDescription = "Search Icon",
                tint = Color.Gray
            )

            BasicTextField(
                value = "",
                onValueChange = {

                },
                readOnly = false,
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                        if (it.isFocused && !focusState.value) {
                            focusState.value = true
                            onClick()
                            focusManager.clearFocus(true)
                        }
                        if (!it.isFocused) {
                            focusState.value = false
                        }
                    }
                    .padding(start = 0.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
                    .weight(1f),
                cursorBrush = SolidColor(Color.White),
                textStyle = TextStyle(
                    color = Color.White,
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Row(
                    ) {

                        Text(
                            modifier = Modifier,
                            color = Color.Gray,
                            text = "Search for a city or airport",
                            fontSize = 16.sp
                        )

                        innerTextField()
                    }

                }
            )
        }
    }

}



