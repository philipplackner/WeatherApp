package com.plcoding.weatherapp.domain.repository
import com.plcoding.weatherapp.data.dto.ForecastDataDto
import com.plcoding.weatherapp.data.dto.ForecastDto
import com.plcoding.weatherapp.domain.util.Resource
import com.plcoding.weatherapp.domain.weather.WeatherInfo

interface WeatherRepository {
    suspend fun getWeatherData(lat: Double, long: Double): Resource<WeatherInfo>
    suspend fun getForecast(lat: Double, long: Double): Resource<ForecastDto>
}