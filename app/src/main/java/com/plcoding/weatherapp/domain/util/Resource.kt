package com.plcoding.weatherapp.domain.util

import android.util.Log
import com.plcoding.weatherapp.data.dto.ForecastDataDto
import com.plcoding.weatherapp.data.dto.ForecastDto
import com.plcoding.weatherapp.data.mappers.toWeatherInfo
import com.plcoding.weatherapp.data.dto.WeatherDto
import com.plcoding.weatherapp.data.mappers.toForecastInfo
import com.plcoding.weatherapp.domain.weather.WeatherInfo
import retrofit2.Response

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(data: T? = null) : Resource<T>(data)
}

fun checkResponse(response: Response<WeatherDto>): Resource<WeatherInfo> {
    return if (response.isSuccessful) {
        response.body()?.let {
            Resource.Success(it.toWeatherInfo())
        } ?: run {
            Resource.Error("Empty body")
        }
    } else {
        if (response.code().toString().startsWith("5")) {
            Resource.Error("Server error")
        } else {
            Resource.Error(response.errorBody()?.string() ?: "Default Error")
        }
    }
}

fun checkResponse2(response: Response<ForecastDto>): Resource<ForecastDto> {
    Log.i("TAG", "checkResponse2: ")
    return if (response.isSuccessful) {
        response.body()?.let {
            Resource.Success(it)
        } ?: run {
            Resource.Error("Empty body")
        }
    } else {
        if (response.code().toString().startsWith("5")) {
            Resource.Error("Server error")
        } else {
            Resource.Error(response.errorBody()?.string() ?: "Default Error")
        }
    }
}

