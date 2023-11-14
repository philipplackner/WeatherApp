package com.plcoding.weatherapp.data.dto

import com.squareup.moshi.Json

data class ForecastDto (
    @field:Json(name = "daily")
    val forecastDataDto: ForecastDataDto

)