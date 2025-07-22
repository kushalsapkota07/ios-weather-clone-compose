package com.example.weatherappcompose.viewmodel

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherappcompose.R
import com.example.weatherappcompose.ktor_api.HttpRoutes
import com.example.weatherappcompose.ktor_api.OpenWeatherService
import com.example.weatherappcompose.db.SimpleLocation
import com.example.weatherappcompose.db.WeatherResponse
import com.example.weatherappcompose.db.WeatherSummary
import com.example.weatherappcompose.db.toEntity
import com.example.weatherappcompose.db.toOpenWeatherResponse
import com.example.weatherappcompose.location_api.AutoCompleteResult
import com.example.weatherappcompose.open_meteo.OpenWeatherResponse
import com.example.weatherappcompose.repository.LocationRepository
import com.example.weatherappcompose.repository.PreferencesLastUpdatedRepository
import com.example.weatherappcompose.ui.theme.states.UiState
import com.example.weatherappcompose.wmo.wmoCodeMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import java.time.LocalDateTime


class LocationViewModel(
    app: Application,
    private val openWeatherService: OpenWeatherService,
    private val locationRepository: LocationRepository,
    private val lastUpdatedRepository: PreferencesLastUpdatedRepository,
    private val placesClient: PlacesClient

): ViewModel(), KoinComponent {

    private val _weatherState = MutableStateFlow<UiState<OpenWeatherResponse>>(UiState.Loading())
    private val _savedLocations = MutableStateFlow<List<SimpleLocation>>(emptyList())
    private val _weatherSummaries = MutableStateFlow<List<WeatherSummary>>(emptyList())
    private val _weatherStateDb = MutableStateFlow<UiState<OpenWeatherResponse>>(UiState.Loading())
    private val _weatherDbValue = MutableStateFlow<UiState<OpenWeatherResponse>>(UiState.Loading())
    private val _imageResource = MutableStateFlow(R.raw.clear_day)

    val locationAutoFill = mutableStateListOf<AutoCompleteResult>()
    private var job: Job? = null
    private var currentLatLong by mutableStateOf(LatLng(0.0, 0.0))

    val weatherState: StateFlow<UiState<OpenWeatherResponse>> = _weatherState.asStateFlow()
    val savedLocations: StateFlow<List<SimpleLocation>> = _savedLocations.asStateFlow()
    val weatherSummaries: StateFlow<List<WeatherSummary>> = _weatherSummaries.asStateFlow()
    val weatherDbValue: StateFlow<UiState<OpenWeatherResponse>> = _weatherDbValue.asStateFlow()

    val lastUpdated = lastUpdatedRepository.dateTimeFlow
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            null
        )

    fun updateLastUpdated(newDateTime: LocalDateTime){
        viewModelScope.launch {
            lastUpdatedRepository.saveDateTime(newDateTime)
        }
    }

    fun searchPlaces(query: String){
        job?.cancel()
        locationAutoFill.clear()
        job = viewModelScope.launch {
            val request = FindAutocompletePredictionsRequest
                .builder()
                .setQuery(query)
                .build()
            placesClient
                .findAutocompletePredictions(request)
                .addOnSuccessListener { response ->
                    locationAutoFill += response.autocompletePredictions.map {
                        AutoCompleteResult(
                            it.getFullText(null).toString(),
                            it.placeId
                        )
                    }
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    println(it.cause)
                    println(it.message)
                }
        }
    }

    fun getCoordinates(result: AutoCompleteResult, onResult: (LatLng)-> Unit){
        val placesFields = listOf(Place.Field.LOCATION)
        val request = FetchPlaceRequest.newInstance(result.placeId,placesFields)
        placesClient.fetchPlace(request)
            .addOnSuccessListener {
                if(it!=null){
                    currentLatLong = it.place.location?: LatLng(0.0,0.0)
                    onResult(currentLatLong)

                }
            }
            .addOnFailureListener{
                it.printStackTrace()
                onResult(LatLng(0.0,0.0))
            }
    }

    fun getCurrentWeather(latitude: Double, longitude: Double){
        viewModelScope.launch {
            try {
                HttpRoutes.LATITUDE = latitude
                HttpRoutes.LONGITUDE = longitude
                _weatherState.value = UiState.Loading()
                val response = openWeatherService.getCurrentWeather()
                _weatherState.value = UiState.Success(response)
            } catch (e:Exception){
                _weatherState.value = UiState.Error("Error fetching weather: ${e.message}")
            }
        }.invokeOnCompletion {
            getWeatherImageResource(latitude,longitude)
        }
    }

    fun getCurrentWeatherDbValue(latitude: Double, longitude: Double): Job {

        _weatherDbValue.value = UiState.Loading()
         return viewModelScope.launch(Dispatchers.IO) {
            try {
                HttpRoutes.LATITUDE = latitude
                HttpRoutes.LONGITUDE = longitude
                _weatherDbValue.value = UiState.Loading()
                val response = locationRepository.getWeather(latitude,longitude)
                _weatherDbValue.value = UiState.Success(response.toOpenWeatherResponse())
            } catch (e:Exception){
                _weatherDbValue.value = UiState.Error("Error fetching weather: ${e.message}")
            }

        }

    }

     fun getWeatherImageResource(lat: Double, lon:Double){

         viewModelScope.launch {
             locationRepository.getWeatherImageResource(lat, lon).collectLatest {
                 it?.let {
                     when(it.isDay){
                         1 -> {
                             _imageResource.value = wmoCodeMap[it.weatherCode]?.day?.image ?: R.raw.clear_day
                         }
                         0 -> {
                             _imageResource.value = wmoCodeMap[it.weatherCode]?.night?.image ?: R.raw.clear_night
                         }
                     }
                 }
             }

         }
    }


    fun saveLocationAndWeather(simpleLocation: SimpleLocation, weatherResponse: WeatherResponse){
        viewModelScope.launch {
            locationRepository.insertLocationAndWeather(simpleLocation, weatherResponse)
        }
    }


    fun deleteLocationData(simpleLocation: SimpleLocation){
        viewModelScope.launch {
            try {
                locationRepository.deleteLocation(simpleLocation)
            } catch (e: SQLiteConstraintException){
                println("Message : ${e.message}")
                println("Cause : ${e.cause}")
            }
        }
    }

    private fun getSavedLocations(){
        viewModelScope.launch {
            locationRepository.getSavedLocations().collect{
                _savedLocations.value = it.keys.toList()
                _weatherSummaries.value = it.values.toList()
            }
        }
    }

    init {
        getSavedLocations()
    }

      fun refreshWeatherData(){
        getSavedLocations()
        viewModelScope.launch {
                val locations = locationRepository.getAllLocations().first()
                for (loc in locations) {
                    getCurrentWeatherDbValue(loc.lat, loc.lon).join()

                            when(val currentState = _weatherStateDb.value){

                                is UiState.Loading -> {
                                    println("Refreshing data..")
                                }

                                is UiState.Error<OpenWeatherResponse> -> {
                                    viewModelScope.launch {
                                        getCurrentWeatherDbValue(loc.lat,loc.lon)
                                    }
                                    Log.e("API Fetch Error","Message: ${currentState.message}")
                                }

                                is UiState.Success<OpenWeatherResponse> -> {
                                    println(
                                        " Successfully refreshed data.."
                                    )
                                    viewModelScope.launch {
                                    locationRepository.insertWeather(
                                        (currentState.value).toEntity(
                                            loc.lat,
                                            loc.lon
                                        )
                                    )
                                        updateLastUpdated(LocalDateTime.now())
                                }
                            }
                    }

                }
        }
    }
}