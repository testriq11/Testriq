package com.example.testriq.gps_module

import android.location.Location
import android.location.LocationManager

class FakeLocationProvider (private val locationManager: LocationManager) {
    fun setFakeLocation(latitude: Double, longitude: Double) {
        val fakeLocation = Location(LocationManager.GPS_PROVIDER)
        fakeLocation.latitude = latitude
        fakeLocation.longitude = longitude
        fakeLocation.accuracy = 1.0f
        fakeLocation.time = System.currentTimeMillis()
        locationManager.setTestProviderLocation(LocationManager.GPS_PROVIDER, fakeLocation)
    }
}