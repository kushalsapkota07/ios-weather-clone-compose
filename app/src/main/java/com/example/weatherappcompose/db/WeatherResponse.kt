package com.example.weatherappcompose.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.example.weatherappcompose.open_meteo.Current
import com.example.weatherappcompose.open_meteo.Daily
import com.example.weatherappcompose.open_meteo.OpenWeatherResponse
import com.example.weatherappcompose.wmo.getWeatherCondition
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.math.roundToInt


@Entity(
    tableName = "weather_response",
    primaryKeys = ["lat","lon"],
    foreignKeys = [
        ForeignKey(
            entity = SimpleLocation::class,
            parentColumns = ["lat","lon"],
            childColumns = ["lat","lon"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(
            value = ["lat","lon"]
        )
    ]
)
@Serializable
data class WeatherResponse(
    val current_json: String,
    val current_units_json: String,
    val daily_json: String,
    val daily_units_json: String,
    val elevation: Double,
    val generationtime_ms: Double,
    val hourly_json: String,
    val hourly_units_json: String,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val timezone_abbreviation: String,
    val utc_offset_seconds: Int,
    val isDay: Int,
    val weatherCode:Int = 0,
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val created_at: Long = System.currentTimeMillis(),
    val updated_at: Long = System.currentTimeMillis()

)
        fun OpenWeatherResponse.toEntity(lat: Double,lon: Double): WeatherResponse{
            return WeatherResponse(
                current_json = Json.encodeToString(current),
                current_units_json = Json.encodeToString(current_units),
                daily_json = Json.encodeToString(daily),
                daily_units_json = Json.encodeToString(daily_units),
                hourly_json = Json.encodeToString(hourly),
                hourly_units_json = Json.encodeToString(hourly_units),
                elevation = elevation,
                latitude = latitude,
                longitude = longitude,
                generationtime_ms = generationtime_ms,
                timezone = timezone,
                timezone_abbreviation = timezone_abbreviation,
                utc_offset_seconds = utc_offset_seconds,
                lat = lat,
                lon = lon,
                isDay = current.is_day,
                weatherCode = current.weather_code
            )
        }

    fun WeatherResponse.toOpenWeatherResponse(): OpenWeatherResponse{
        return OpenWeatherResponse(
            current= Json.decodeFromString(current_json),
            current_units= Json.decodeFromString(current_units_json),
        daily= Json.decodeFromString(daily_json),
        daily_units= Json.decodeFromString(daily_units_json),
        elevation= elevation,
        generationtime_ms= generationtime_ms,
        hourly= Json.decodeFromString(hourly_json),
        hourly_units= Json.decodeFromString(hourly_units_json),
        latitude= latitude,
        longitude= longitude,
        timezone= timezone,
        timezone_abbreviation= timezone_abbreviation,
        utc_offset_seconds= utc_offset_seconds,
    )
    }

fun WeatherSummary.toCurrentValues(): List<String>{

    val currentValues = Json.decodeFromString<Current>(this.current_json)
    val dailyValues = Json.decodeFromString<Daily>(this.daily_json)
    val condition = getWeatherCondition(currentValues.weather_code,
        currentValues.is_day == 1
    )
    val minTemp = dailyValues.temperature_2m_min[0].roundToInt()
    val maxTemp = dailyValues.temperature_2m_max[0].roundToInt()
    return listOf(condition.description,currentValues.temperature_2m.roundToInt().toString(), minTemp.toString(),maxTemp.toString(),condition.image.toString())
}