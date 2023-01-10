package com.plcoding.weatherapp.data.location

import android.app.Application
import android.location.Geocoder
import com.plcoding.weatherapp.domain.location.LocationTracker
import java.util.*
import javax.inject.Inject

class CityProvider () {
    suspend fun getCity( lat:Double?,  long:Double?): String {
        if (lat == null || long == null) return "null"
        return Geocoder(
            application.applicationContext,
            Locale.getDefault()
        )
            .getFromLocation(lat, long, 1)[0]
            .locality
    }
}
