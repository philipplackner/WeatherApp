package com.plcoding.weatherapp.data.repository

import com.plcoding.weatherapp.data.dto.ForecastDataDto
import com.plcoding.weatherapp.data.dto.ForecastDto
import com.plcoding.weatherapp.data.remote.WeatherApi
import com.plcoding.weatherapp.domain.repository.WeatherRepository
import com.plcoding.weatherapp.domain.util.Resource
import com.plcoding.weatherapp.domain.util.checkResponse
import com.plcoding.weatherapp.domain.util.checkResponse2
import com.plcoding.weatherapp.domain.weather.WeatherInfo
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi
) : WeatherRepository {

    override suspend fun getWeatherData(lat: Double, long: Double): Resource<WeatherInfo> =
        checkResponse(api.getWeatherData(lat, long))

    override suspend fun getForecast(lat: Double, long: Double): Resource<ForecastDto> =
        checkResponse2(api.getDailyForecast(lat,long))
}