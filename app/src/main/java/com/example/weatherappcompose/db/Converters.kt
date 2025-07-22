package com.example.weatherappcompose.db

import androidx.room.TypeConverter
import com.example.weatherappcompose.open_meteo.Current
import com.example.weatherappcompose.open_meteo.CurrentUnits
import com.example.weatherappcompose.open_meteo.Daily
import com.example.weatherappcompose.open_meteo.DailyUnits
import com.example.weatherappcompose.open_meteo.Hourly
import com.example.weatherappcompose.open_meteo.HourlyUnits
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class Converters {

    private val json = Json{ignoreUnknownKeys = true}

    //current converters
    @TypeConverter
    fun fromCurrent(current: Current):String = json.encodeToString(current)
    @TypeConverter
    fun toCurrent(data: String): Current = json.decodeFromString(data)

    @TypeConverter
    fun fromCurrentUnits(currentUnits: CurrentUnits):String = json.encodeToString(currentUnits)
    @TypeConverter
    fun toCurrentUnits(data: String): CurrentUnits = json.decodeFromString(data)

    // hourly converters
    @TypeConverter
    fun fromHourly(hourly: Hourly):String = json.encodeToString(hourly)
    @TypeConverter
    fun toHourly(data:String):Hourly = json.decodeFromString(data)

    @TypeConverter
    fun fromHourlyUnits(hourlyUnits: HourlyUnits):String = json.encodeToString(hourlyUnits)
    @TypeConverter
    fun toHourlyUnits(data: String): HourlyUnits = json.decodeFromString(data)

    //daily converters
    @TypeConverter
    fun fromDaily(daily: Daily):String = json.encodeToString(daily)
    @TypeConverter
    fun toDaily(data:String):Daily = json.decodeFromString(data)

    @TypeConverter
    fun fromDailyUnits(dailyUnits: DailyUnits):String = json.encodeToString(dailyUnits)
    @TypeConverter
    fun toDailyUnits(data: String): DailyUnits = json.decodeFromString(data)
}