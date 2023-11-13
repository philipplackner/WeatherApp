package com.plcoding.weatherapp.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.weatherapp.domain.location.LocationTracker
import com.plcoding.weatherapp.domain.usecase.GetWeatherUseCase
import com.plcoding.weatherapp.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val locationTracker: LocationTracker
) : ViewModel() {

    var state by mutableStateOf(WeatherState())
        private set

    fun loadWeatherInfo() {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                error = null
            )
            locationTracker.getCurrentLocation()?.let { location ->
                getWeatherUseCase.invoke(location.latitude, location.longitude).catch {
                    Log.e("WeatherViewModel", "loadWeatherInfo: ", it)
                    state = state.copy(
                        weatherInfo = null,
                        isLoading = false,
                        error = it.localizedMessage
                    )
                }.collect {
                    when (it) {
                        is Resource.Success -> {
                            state = state.copy(
                                weatherInfo = it.data,
                                isLoading = false,
                                error = null
                            )
                        }

                        is Resource.Error -> {
                            state = state.copy(
                                weatherInfo = null,
                                isLoading = false,
                                error = it.message
                            )
                        }

                        is Resource.Loading -> {
                            state = state.copy(
                                weatherInfo = null,
                                isLoading = true,
                                error = null
                            )
                        }
                    }
                }
            } ?: kotlin.run {
                state = state.copy(
                    isLoading = false,
                    error = "Couldn't retrieve location. Make sure to grant permission and enable GPS."
                )
            }
        }
    }
}