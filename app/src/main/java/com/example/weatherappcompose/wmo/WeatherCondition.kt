package com.example.weatherappcompose.wmo

import com.example.weatherappcompose.R
import kotlinx.serialization.Serializable

@Serializable
data class WeatherCondition(
    val description: String = "Unknown",
    val drawable: Int = R.drawable.clear_day,
    val image:Int = R.raw.clear_day
)

@Serializable
data class WeatherCodeEntry(
    val day: WeatherCondition = WeatherCondition(),
    val night: WeatherCondition = WeatherCondition()
)

val wmoCodeMap = mapOf(
    0 to WeatherCodeEntry(
        day = WeatherCondition("Sunny", R.drawable.clear_day, R.raw.clear_day),
        night = WeatherCondition("Clear", R.drawable.clear_night, R.raw.clear_night)
    ),
    1 to WeatherCodeEntry(
        day = WeatherCondition("Mainly Sunny", R.drawable.mostly_clear_day, R.raw.mainly_sunny),
        night = WeatherCondition("Mainly Clear", R.drawable.mostly_clear_night, R.raw.mainly_clear)
    ),
    2 to WeatherCodeEntry(
        day = WeatherCondition("Partly Cloudy", R.drawable.partly_cloudy_day, R.raw.partly_cloudy_day),
        night = WeatherCondition("Partly Cloudy", R.drawable.partly_cloudy_night, R.raw.partly_cloudy_night)
    ),
    3 to WeatherCodeEntry(
        day = WeatherCondition("Cloudy", R.drawable.cloudy, R.raw.cloudy_day),
        night = WeatherCondition("Cloudy", R.drawable.cloudy_night, R.raw.cloudy_night)
    ),
    45 to WeatherCodeEntry(
        day = WeatherCondition("Foggy", R.drawable.fog, R.raw.foggy_day),
        night = WeatherCondition("Foggy", R.drawable.fog, R.raw.foggy_night)
    ),
    48 to WeatherCodeEntry(
        day = WeatherCondition("Rime Fog", R.drawable.fog, R.raw.foggy_day),
        night = WeatherCondition("Rime Fog", R.drawable.fog, R.raw.foggy_night)
    ),
    51 to WeatherCodeEntry(
        day = WeatherCondition("Light Drizzle", R.drawable.drizzle, R.raw.light_rain),
        night = WeatherCondition("Light Drizzle", R.drawable.drizzle, R.raw.light_rain_night)
    ),
    53 to WeatherCodeEntry(
        day = WeatherCondition("Drizzle", R.drawable.drizzle, R.raw.light_rain),
        night = WeatherCondition("Drizzle", R.drawable.drizzle, R.raw.light_rain_night)
    ),
    55 to WeatherCodeEntry(
        day = WeatherCondition("Heavy Drizzle", R.drawable.drizzle, R.raw.light_rain),
        night = WeatherCondition("Heavy Drizzle", R.drawable.drizzle, R.raw.light_rain_night)
    ),
    56 to WeatherCodeEntry(
        day = WeatherCondition("Light Freezing Drizzle", R.drawable.freezingdrizzle, R.raw.light_rain),
        night = WeatherCondition("Light Freezing Drizzle", R.drawable.freezingdrizzle, R.raw.light_rain_night)
    ),
    57 to WeatherCodeEntry(
        day = WeatherCondition("Freezing Drizzle", R.drawable.freezingdrizzle, R.raw.light_rain),
        night = WeatherCondition("Freezing Drizzle", R.drawable.freezingdrizzle, R.raw.light_rain_night)
    ),
    61 to WeatherCodeEntry(
        day = WeatherCondition("Light Rain", R.drawable.rain, R.raw.light_rain),
        night = WeatherCondition("Light Rain", R.drawable.rain, R.raw.light_rain_night)
    ),
    63 to WeatherCodeEntry(
        day = WeatherCondition("Rain", R.drawable.rain, R.raw.heavy_rain),
        night = WeatherCondition("Rain", R.drawable.rain, R.raw.heavy_rain_night)
    ),
    65 to WeatherCodeEntry(
        day = WeatherCondition("Heavy Rain", R.drawable.rain, R.raw.heavy_rain),
        night = WeatherCondition("Heavy Rain", R.drawable.rain, R.raw.heavy_rain_night)
    ),
    66 to WeatherCodeEntry(
        day = WeatherCondition("Light Freezing Rain", R.drawable.freezingrain, R.raw.heavy_rain),
        night = WeatherCondition("Light Freezing Rain", R.drawable.freezingrain, R.raw.heavy_rain_night)
    ),
    67 to WeatherCodeEntry(
        day = WeatherCondition("Freezing Rain", R.drawable.freezingrain, R.raw.heavy_rain),
        night = WeatherCondition("Freezing Rain", R.drawable.freezingrain, R.raw.heavy_rain_night)
    ),
    71 to WeatherCodeEntry(
        day = WeatherCondition("Light Snow", R.drawable.snow, R.raw.snow),
        night = WeatherCondition("Light Snow", R.drawable.snow, R.raw.snow_night)
    ),
    73 to WeatherCodeEntry(
        day = WeatherCondition("Snow", R.drawable.snow, R.raw.snow),
        night = WeatherCondition("Snow", R.drawable.snow, R.raw.snow_night)
    ),
    75 to WeatherCodeEntry(
        day = WeatherCondition("Heavy Snow", R.drawable.snow, R.raw.snow),
        night = WeatherCondition("Heavy Snow", R.drawable.snow, R.raw.snow_night)
    ),
    77 to WeatherCodeEntry(
        day = WeatherCondition("Snow Grains", R.drawable.snowflake, R.raw.snow),
        night = WeatherCondition("Snow Grains", R.drawable.snowflake, R.raw.snow_night)
    ),
    80 to WeatherCodeEntry(
        day = WeatherCondition("Light Showers", R.drawable.rain, R.raw.heavy_rain),
        night = WeatherCondition("Light Showers", R.drawable.rain, R.raw.heavy_rain_night)
    ),
    81 to WeatherCodeEntry(
        day = WeatherCondition("Showers", R.drawable.rain,R.raw.heavy_rain),
        night = WeatherCondition("Showers", R.drawable.rain, R.raw.heavy_rain_night)
    ),
    82 to WeatherCodeEntry(
        day = WeatherCondition("Heavy Showers", R.drawable.rain, R.raw.heavy_rain),
        night = WeatherCondition("Heavy Showers", R.drawable.rain, R.raw.heavy_rain_night)
    ),
    85 to WeatherCodeEntry(
        day = WeatherCondition("Light Snow Showers", R.drawable.snowflake, R.raw.snow),
        night = WeatherCondition("Light Snow Showers", R.drawable.snowflake, R.raw.snow_night)
    ),
    86 to WeatherCodeEntry(
        day = WeatherCondition("Snow Showers", R.drawable.snow, R.raw.snow),
        night = WeatherCondition("Snow Showers", R.drawable.snow, R.raw.snow_night)
    ),
    95 to WeatherCodeEntry(
        day = WeatherCondition("Thunderstorm", R.drawable.thunderstorm, R.raw.thunderstorm),
        night = WeatherCondition("Thunderstorm", R.drawable.thunderstorm, R.raw.thunderstorm_night)
    ),
    96 to WeatherCodeEntry(
        day = WeatherCondition("Light Thunderstorms With Hail", R.drawable.thunderstorm_hail, R.raw.thunderstorm),
        night = WeatherCondition("Light Thunderstorms With Hail", R.drawable.thunderstorm_hail, R.raw.thunderstorm_night)
    ),
    99 to WeatherCodeEntry(
        day = WeatherCondition("Thunderstorm With Hail", R.drawable.thunderstorm_hail, R.raw.thunderstorm),
        night = WeatherCondition("Thunderstorm With Hail", R.drawable.thunderstorm_hail, R.raw.thunderstorm_night)
    )
)

fun getWeatherCondition(code: Int, isDayTime: Boolean): WeatherCondition{
    val entry = wmoCodeMap[code] ?: return WeatherCondition()
    return if (isDayTime) entry.day else entry.night
}


