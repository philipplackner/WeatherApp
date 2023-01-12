package com.plcoding.weatherapp.presentation

import android.app.Application
import android.location.Geocoder
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
import java.util.*
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val locationTracker: LocationTracker,
    private val app:Application
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
                when (val result = repository.getWeatherData(location.latitude, location.longitude)) {
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
            } ?: kotlin.run {
                state = state.copy(
                    isLoading = false,
                    error = "Couldn't retrieve location. Make sure to grant permission and enable GPS."
                )
            }
        }
    }
        fun loadCity() {
        viewModelScope.launch {
            locationTracker.getCurrentLocation()?.let { location ->
                val lat = locationTracker.getCurrentLocation()!!.latitude
                val long = locationTracker.getCurrentLocation()!!.longitude
                var cityName: String?
                val geoCoder = Geocoder(app.applicationContext, Locale.getDefault())
                val address = geoCoder.getFromLocation(lat, long, 1)
                cityName = address[0].adminArea
                if (cityName == null) {
                    cityName = address[0].locality
                    if (cityName == null) {
                        cityName = address[0].subAdminArea
                    }
                }
                state.weatherInfo?.currentWeatherData?.city = cityName
            }
        }
    }
}
