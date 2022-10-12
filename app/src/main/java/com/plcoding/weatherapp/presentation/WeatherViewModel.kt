package com.plcoding.weatherapp.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.weatherapp.domain.location.LocationTracker
import com.plcoding.weatherapp.domain.repository.WeatherRepository
import com.plcoding.weatherapp.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val locationTracker: LocationTracker
): ViewModel() {
    var state by mutableStateOf(WeatherState())
        private set

    fun loadWeatherInfo() {
        //launch coroutine
        viewModelScope.launch {
            //update state to show loading indicator
            state = state.copy(
                isLoading = true,
                error = null
            )
            //get current location then if not null do let for location
            locationTracker.getCurrentLocation()?.let { location ->
                //get weather data for location
                //when result success set weatherInfo in state accordingly
                when(val result = repository.getWeatherData(location.latitude, location.longitude)){
                    is Resource.Success -> {
                        state = state.copy(
                            weatherInfo = result.data,
                            isLoading = false,
                            error = null
                        )
                    }
                    is Resource.Error -> {
                        state = state.copy(
                            weatherInfo = null,
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            } ?: kotlin.run {  //if location is null
                state = state.copy(
                    isLoading = false,
                    error = "Couldn't retreive location. please enable location, and grant permissions!"
                )
            }
        }
    }
}