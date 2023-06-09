package com.example.testriq.day_analyzer_module

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.app.ActivityCompat

class LocationTracker(private val context: Context) : LocationListener {

    private val locationManager: LocationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    // Callback to receive location updates
    private var locationCallback: ((Location) -> Unit)? = null

    fun startLocationUpdates(callback: (Location) -> Unit) {
        // Save the callback
        locationCallback = callback

        // Request location updates
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0f,
                this
            )
        }
    }

    fun stopLocationUpdates() {
        // Remove location updates
        locationManager.removeUpdates(this)
        locationCallback = null
    }

    override fun onLocationChanged(location: Location) {
        // Callback with the new location
        locationCallback?.invoke(location)
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        // Not needed for this example
    }

    override fun onProviderEnabled(provider: String) {
        // Not needed for this example
    }

    override fun onProviderDisabled(provider: String) {
        // Not needed for this example
    }
}