package com.plcoding.weatherapp.data.dto

import com.squareup.moshi.Json
import java.time.LocalDateTime
import java.util.Date

data class ForecastDataDto (
    val time: List<String>,
    @field:Json(name = "temperature_2m_max")
    val maxTemperatures: List<Double>,
    @field:Json(name = "temperature_2m_min")
    val minTemperatures : List<Double>,
    @field:Json(name = "weathercode")
    val weatherCodes: List<Int>,
    @field:Json(name = "sunrise")
    val sunrise: List<String>,
    @field:Json(name = "sunset")
    val sunset: List<String>,
    @field:Json(name = "rain_sum")
    val rainSum: List<Double>,
    @field:Json(name = "showers_sum")
    val showersSum: List<Double>,
    @field:Json(name = "snowfall_sum")
    val snowfallSum: List<Double>

)