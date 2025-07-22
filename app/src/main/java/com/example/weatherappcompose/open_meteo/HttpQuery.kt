package com.example.weatherappcompose.open_meteo

import androidx.room.Entity
import androidx.room.ForeignKey
import kotlinx.serialization.Serializable

@Entity(
    tableName = "http_query",
    foreignKeys = [
        ForeignKey(
            entity = OpenWeatherResponse::class,
            parentColumns = arrayOf("weather_id"),
            childColumns = arrayOf("weather_id"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE

        )
    ]
)
@Serializable

data class HttpQuery(
    val weather_id: Int,
    val query: String
)