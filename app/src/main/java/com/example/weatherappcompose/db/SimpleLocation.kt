package com.example.weatherappcompose.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(
    tableName = "location_data",
    primaryKeys = ["lat","lon"]
)
@Serializable
data class SimpleLocation(
    var name:String="",
    var lat: Double=0.0,
    var lon: Double=0.0,
    var priority: Int=0,
    var time: String="",
    val query: String = ""
)