package com.example.weatherappcompose.ktor_api


object HttpRoutes {

    const val BASE_URL = "https://api.open-meteo.com/v1/forecast"

    var LATITUDE: Double = 0.00
    var LONGITUDE:Double = 0.00
    const val TIMEZONE: String = "Australia/Sydney"
    const val FORECAST_DAYS: Int = 14

    const val CURRENT = "temperature_2m,apparent_temperature,is_day,precipitation,rain,snowfall,weather_code,surface_pressure,wind_speed_10m,wind_direction_10m,wind_gusts_10m"
    const val HOURLY = "temperature_2m,relative_humidity_2m,dew_point_2m,apparent_temperature,precipitation_probability,precipitation,rain,weather_code,visibility,wind_speed_10m,wind_gusts_10m,uv_index,is_day,sunshine_duration,temperature_1000hPa,uv_index"
    const val DAILY = "weather_code,temperature_2m_max,temperature_2m_min,sunrise,sunset,daylight_duration,sunshine_duration,uv_index_max,uv_index_clear_sky_max,precipitation_probability_max,precipitation_sum"
}