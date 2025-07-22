package com.example.weatherappcompose.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [SimpleLocation::class, WeatherResponse::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class LocationDatabase : RoomDatabase(){
    abstract fun getSimpleLocationDao(): SimpleLocationDao
}