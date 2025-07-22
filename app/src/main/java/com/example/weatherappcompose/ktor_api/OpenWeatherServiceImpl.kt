package com.example.weatherappcompose.ktor_api

import com.example.weatherappcompose.open_meteo.OpenWeatherResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url

class OpenWeatherServiceImpl(
    private val client: HttpClient,
) : OpenWeatherService {

    override suspend fun getCurrentWeather(): OpenWeatherResponse {

        return try {
            client.get {
                url(HttpRoutes.BASE_URL)
                parameter("latitude",HttpRoutes.LATITUDE)
                parameter("longitude",HttpRoutes.LONGITUDE)
                parameter("current",HttpRoutes.CURRENT)
                parameter("hourly",HttpRoutes.HOURLY)
                parameter("daily",HttpRoutes.DAILY)
                parameter("timezone", HttpRoutes.TIMEZONE)
                parameter("forecast_days",HttpRoutes.FORECAST_DAYS)
            }.body<OpenWeatherResponse>()
        } catch (e: RedirectResponseException){
            // 3xx - responses
            println("Error: ${e.response.status.description}")
            throw NoSuchElementException("3xx- Redirect Response Exception")

        } catch (e: ClientRequestException){
            // 3xx - responses
            println("Error: ${e.response.status.description}")
            throw NoSuchElementException("4xx - Client Request Exception")

        } catch (e: ServerResponseException){
            // 3xx - responses
            println("Error: ${e.response.status.description}")
            throw NoSuchElementException("5xx - Server Response Exception")

        } catch (e: Exception){
            //General Exceptions
            println("Error: ${e.message}")
            throw NoSuchElementException("General Exception")
        }
    }


}