package com.example.weatherappcompose.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.weatherappcompose.open_meteo.OpenWeatherResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface SimpleLocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(simpleLocation: SimpleLocation): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWeather(weatherResponse: WeatherResponse): Long

    @Update
    suspend fun updateWeather(weatherResponse: WeatherResponse): Int

    @Query(" SELECT * FROM location_data WHERE lat = :lat AND lon= :lon")
    suspend fun getLocation(lat: Double,lon: Double): SimpleLocation

    @Query(" SELECT * FROM location_data")
    fun getAllLocations(): Flow<List<SimpleLocation>>

    @Query(" SELECT weather_response.current_json , weather_response.daily_json FROM weather_response WHERE" +
            " lat = :lat AND lon = :lon")
    fun getWeatherSummaryFlow(lat: Double,lon: Double): Flow<WeatherSummary>

    @Query(" SELECT * FROM weather_response WHERE lat = :lat AND lon = :lon")
    suspend fun getWeather(lat: Double, lon: Double): WeatherResponse?

    @Query("SELECT * FROM weather_response WHERE lat= :lat AND lon= :lon ")
    fun getWeatherFlow(lat: Double,lon: Double): WeatherResponse

    @Query(" SELECT MAX(priority) FROM location_data ")
    suspend fun getMaxPriority():Int?

    @Delete()
    suspend fun deleteLocation(simpleLocation: SimpleLocation)

    @Query(" DELETE FROM weather_response WHERE weather_response.lat = :lat AND weather_response.lon = :lon  ")
    suspend fun deleteWeather(lat:Double, lon:Double)

    @Query("DELETE FROM weather_response")
    suspend fun deleteAll();

    @Query("SELECT isDay  , weatherCode  " +
            "FROM weather_response WHERE lat = :lat " +
            " AND lon = :lon ")
     fun getWeatherImageResource(lat: Double,lon: Double): Flow<WeatherImageCode>

    @Query(
        " SELECT location_data.*, "+
        " weather_response.current_json , weather_response.daily_json "+
        "FROM location_data"+
        " JOIN weather_response ON location_data.lat = weather_response.lat  "+
        " AND location_data.lon = weather_response.lon "+
        "ORDER BY location_data.priority DESC "
    )
    fun getAll(): Flow<Map<SimpleLocation, WeatherSummary>>

    @Query("DELETE FROM weather_response")
    suspend fun deleteAllWeatherData()
}