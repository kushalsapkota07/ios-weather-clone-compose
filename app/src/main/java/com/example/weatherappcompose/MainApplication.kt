package com.example.weatherappcompose

import android.app.Application
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.example.weatherappcompose.modules.appModule
import com.google.android.libraries.places.api.Places
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import java.io.File

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        if (!Places.isInitialized()){
            Places.initialize(applicationContext,BuildConfig.KAP)
        }

        startKoin {

            //Log into android logger
            androidLogger()
            //Reference Android Context
            androidContext(this@MainApplication)
            //Load Modules
            modules(appModule)
        }
    }
}