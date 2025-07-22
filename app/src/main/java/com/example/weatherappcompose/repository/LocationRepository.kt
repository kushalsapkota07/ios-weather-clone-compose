package com.example.weatherappcompose.repository

import androidx.room.Transaction
import com.example.weatherappcompose.db.LocationDatabase
import com.example.weatherappcompose.db.SimpleLocation
import com.example.weatherappcompose.db.WeatherImageCode
import com.example.weatherappcompose.db.WeatherResponse
import com.example.weatherappcompose.db.WeatherSummary
import com.example.weatherappcompose.open_meteo.OpenWeatherResponse
import kotlinx.coroutines.flow.Flow

class LocationRepository(
    private val db: LocationDatabase
) {
    suspend fun insertLocation(simpleLocation: SimpleLocation): Long{
        return db.getSimpleLocationDao().insertLocation(simpleLocation)
    }

    suspend fun insertWeather(weatherResponse: WeatherResponse): Long{
        return db.getSimpleLocationDao().insertWeather(weatherResponse)
    }

    suspend fun getMaxPriority(): Int?{
        return db.getSimpleLocationDao().getMaxPriority()
    }

     fun getWeather(lat: Double, long: Double): WeatherResponse{
        return db.getSimpleLocationDao().getWeatherFlow(lat,long)
    }

     fun getAllLocations(): Flow<List<SimpleLocation>>{
        return db.getSimpleLocationDao().getAllLocations()
    }

    fun getSavedLocations(): Flow<Map<SimpleLocation, WeatherSummary>>{
        return db.getSimpleLocationDao().getAll()
    }

    suspend fun updateWeather(weatherResponse: WeatherResponse): Int{
        return db.getSimpleLocationDao().updateWeather(weatherResponse)
    }

    @Transaction
    suspend fun deleteLocation(simpleLocation: SimpleLocation){
        db.getSimpleLocationDao().deleteWeather(simpleLocation.lat,simpleLocation.lon)
        db.getSimpleLocationDao().deleteLocation(simpleLocation)
    }

    fun getWeatherImageResource(lat: Double,lon: Double): Flow<WeatherImageCode>{
        return db.getSimpleLocationDao().getWeatherImageResource(lat,lon)
    }

    @Transaction
    suspend fun insertLocationAndWeather(
        simpleLocation: SimpleLocation,
        weatherResponse: WeatherResponse
    ){
        db.getSimpleLocationDao().insertLocation(simpleLocation)
        db.getSimpleLocationDao().insertWeather(weatherResponse)
    }

}