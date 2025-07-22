package com.example.weatherappcompose.modules

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.weatherappcompose.db.LocationDatabase
import com.example.weatherappcompose.db.SimpleLocationDao
import com.example.weatherappcompose.ktor_api.OpenWeatherService
import com.example.weatherappcompose.ktor_api.OpenWeatherServiceImpl
import com.example.weatherappcompose.repository.LocationRepository
import com.example.weatherappcompose.repository.PreferencesLastUpdatedRepository
import com.example.weatherappcompose.viewmodel.LocationViewModel
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel

import org.koin.dsl.module
import java.io.File

val appModule = module {

    single {
        HttpClient(Android) {
            install(Logging) {

                level = LogLevel.ALL
            }


            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    }
                )
            }
        }
    }


    single {
        PreferenceDataStoreFactory.create(
            produceFile = { File(androidContext().filesDir, "last_updated.preferences_pb") }
        )
    }

    single<PreferencesLastUpdatedRepository>{
        PreferencesLastUpdatedRepository(get())
    }

    single<OpenWeatherService> {
        OpenWeatherServiceImpl(get())
    }

    single {
        LocationRepository(get<LocationDatabase>())
    }
    single<PlacesClient> {
        Places.createClient(androidContext())
    }
    single {
        Room.databaseBuilder(
            androidApplication(),
            LocationDatabase::class.java,
            "location_db.db"
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                    db.execSQL("PRAGMA foreign_keys=ON")
                }
            })
            .build()
    }

    single<SimpleLocationDao> {
        val database = get<LocationDatabase>()
        database.getSimpleLocationDao()
    }
    viewModel {LocationViewModel(
        app = androidApplication(),
        locationRepository = get<LocationRepository>(),
        openWeatherService = get<OpenWeatherService>(),
        lastUpdatedRepository = get<PreferencesLastUpdatedRepository>(),
        placesClient = get<PlacesClient>()
    ) }

}
