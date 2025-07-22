package com.example.weatherappcompose.open_sdk.helper

import com.example.weatherappcompose.open_meteo.OpenWeatherResponse
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

data class HourlyForecast(val time: String,
                          val weatherCode: Int,
                          val temperature: Int,
                          val apparentTemperature: Int,
                          val visibility: Int,
                          val uvIndex: Int,
                          val contentDescription: String,
                            )

 fun getHourlyForecast(response: OpenWeatherResponse): List<HourlyForecast>{
    val hourlyForecastValues: MutableList<HourlyForecast> = mutableListOf()

    val currentTime = response.current.time
     val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
     val dateTime = LocalDateTime.parse(currentTime, formatter)

     // Set minutes to 00
     val updatedDateTime = dateTime.withMinute(0)

     val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
     val formattedOutput = updatedDateTime.format(outputFormatter)
    val startIndex = response.hourly.time.indexOf(formattedOutput)

    for (i in startIndex..(startIndex+23)){
        hourlyForecastValues.add(
            HourlyForecast(response.hourly.time[i],
                response.hourly.weather_code[i],
                response.hourly.temperature_2m[i].roundToInt(),
                response.hourly.apparent_temperature[i].roundToInt(),
                response.hourly.visibility[i].roundToInt(),
                response.hourly.uv_index[i].roundToInt(),
                "Weather Description")

        )
    }

    return hourlyForecastValues

}