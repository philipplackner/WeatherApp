package com.plcoding.weatherapp.domain.usecase

import com.plcoding.weatherapp.domain.repository.WeatherRepository
import com.plcoding.weatherapp.domain.util.DefaultRetryPolicy
import com.plcoding.weatherapp.domain.util.Resource
import com.plcoding.weatherapp.domain.util.checkError
import com.plcoding.weatherapp.domain.util.retryWithPolicy
import com.plcoding.weatherapp.domain.weather.WeatherInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

class GetWeatherUseCase constructor(
    private val weatherRepository: WeatherRepository
) {

    suspend operator fun invoke(lat : Double, long : Double): Flow<Resource<WeatherInfo>> = flow {
        emit(weatherRepository.getWeatherData(lat, long))
    }.retryWithPolicy(DefaultRetryPolicy())
        .catch { emit(checkError(it)) }
        .onStart { emit(Resource.Loading()) }

}