package com.example.testriq.gps_module

import android.location.Geocoder
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import androidx.lifecycle.viewmodel.CreationExtras.Empty.map
import com.example.testriq.R
import com.example.testriq.databinding.ActivityCameraBinding
import com.example.testriq.databinding.ActivityGpsBinding
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.maps.DirectionsApi
import java.io.IOException
import com.google.maps.GeoApiContext
import com.google.maps.model.DirectionsResult
import com.google.maps.model.TravelMode

class GpsActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var binding:ActivityGpsBinding
    private lateinit var locationManager: LocationManager
    private lateinit var fakeLocationProvider: FakeLocationProvider
    private val AUTOCOMPLETE_REQUEST_CODE = 1
    private lateinit var mMap: GoogleMap
    private lateinit var geocoder: Geocoder
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityGpsBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)




//
//        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapViewFakeGPs) as SupportMapFragment
//        mapFragment.getMapAsync(this)
//        geocoder = Geocoder(this)


        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)
        binding.buttonShowRoute.setOnClickListener {
            val startPlace = binding.editTextStartPlace.text.toString()
            val endPlace = binding.editTextEndPlace.text.toString()

            calculateRoute(startPlace, endPlace)
        }

    }

    private fun calculateRoute(startPlace: String, endPlace: String) {
        // Set up your own API key for Google Maps Directions API
        val apiKey = "AIzaSyBl3HbkGV-HWgtYn0MYNhFPASIKPIj0tN8"
        val geoApiContext = GeoApiContext.Builder()
            .apiKey(apiKey)
            .build()

        val request = DirectionsApi.getDirections(geoApiContext, startPlace, endPlace)
            .mode(TravelMode.DRIVING)
        try {
            val result: DirectionsResult = request.await()
            val route = result.routes[0]

            // Clear previous markers
            googleMap.clear()

            // Add markers for start and end places
            val startLatLng = LatLng(route.legs[0].startLocation.lat, route.legs[0].startLocation.lng)
            val endLatLng = LatLng(route.legs[0].endLocation.lat, route.legs[0].endLocation.lng)
            googleMap.addMarker(MarkerOptions().position(startLatLng).title("Start Place"))
            googleMap.addMarker(MarkerOptions().position(endLatLng).title("End Place"))

            // Zoom and move the camera to the start location
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLatLng, 12f))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isCompassEnabled = true
//
//        binding.buttonSubmit.setOnClickListener {
////            val city = findViewById<EditText>(R.id.editTextCity).text.toString()
//            val city=binding.editTextCity.text.toString()
//            if (city.isNotEmpty()) {
//                // Geocode the city to get its coordinates
//                val geocoder = Geocoder(this)
//                val addressList = geocoder.getFromLocationName(city, 1)
//                if (addressList!!.isNotEmpty()) {
//                    val address = addressList[0]
//                    val latLng = LatLng(address.latitude, address.longitude)
//
//                    // Move the camera to the city's location
//                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
//
//                    // Add a marker at the city's location
//                    googleMap.addMarker(MarkerOptions().position(latLng).title(city))
//                }
//            }
//        }


    }
}