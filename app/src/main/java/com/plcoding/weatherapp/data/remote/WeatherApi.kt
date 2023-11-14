package com.plcoding.weatherapp.data.remote

import com.plcoding.weatherapp.data.dto.ForecastDto
import com.plcoding.weatherapp.data.dto.WeatherDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("v1/forecast?hourly=temperature_2m,weathercode,relativehumidity_2m,windspeed_10m,pressure_msl")
    suspend fun getWeatherData(
        @Query("latitude") lat: Double,
        @Query("longitude") long: Double
    ): Response<WeatherDto>

    @GET("v1/forecast?daily=weather_code,temperature_2m_max,temperature_2m_min,sunrise,sunset,rain_sum,showers_sum,snowfall_sum")
    suspend fun getDailyForecast(
        @Query("latitude") lat: Double,
        @Query("longitude") long: Double
    ) : Response<ForecastDto>

}