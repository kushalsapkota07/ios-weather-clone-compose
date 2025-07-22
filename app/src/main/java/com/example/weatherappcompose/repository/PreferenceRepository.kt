package com.example.weatherappcompose.repository

import android.content.Context
import android.text.format.DateUtils
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId

class PreferencesLastUpdatedRepository(
    private val dataStore: DataStore<Preferences>
){
    companion object{
        private val LAST_UPDATED = stringPreferencesKey("last_updated")
    }
    suspend fun saveDateTime(dateTime: LocalDateTime){
        dataStore.edit { prefs ->
            prefs[LAST_UPDATED] = dateTime.toString()
        }
    }

    val dateTimeFlow: Flow<LocalDateTime?> = dataStore.data
        .map { prefs ->
            prefs[LAST_UPDATED]?.let { LocalDateTime.parse(it) }

        }
}

fun getTimeAgo(dateTime: LocalDateTime): String{

    val now = LocalDateTime.now()
    val duration = Duration.between(dateTime,now)

    val millis = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    val relativeTime = DateUtils.getRelativeTimeSpanString(
        millis,
        System.currentTimeMillis(),
        DateUtils.MINUTE_IN_MILLIS
    )

    return when(relativeTime){
        "0 minutes ago" -> "Just Now"
        else -> relativeTime.toString()
    }

//    return when{
//        duration.toMinutes() < 1 -> "Just Now"
//        duration.toMinutes() < 60 -> "${duration.toMinutes()} minutes ago"
//        duration.toHours() < 24 -> "${duration.toHours()} hour ago"
//        duration.toDays() == 1L -> "Yesterday"
//        else -> ""
//    }
}