package com.example.weatherappcompose.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherappcompose.ktor_api.OpenWeatherService
import com.example.weatherappcompose.repository.LocationRepository
import com.example.weatherappcompose.repository.PreferencesLastUpdatedRepository
import com.google.android.libraries.places.api.net.PlacesClient

class LocationViewModelProviderFactory(
    val app: Application,
    private val openWeatherService: OpenWeatherService,
    private val locationRepository: LocationRepository,
    private val lastUpdatedRepository: PreferencesLastUpdatedRepository,
    private val placesClient: PlacesClient
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LocationViewModel(app, openWeatherService, locationRepository, lastUpdatedRepository, placesClient) as T
    }
}