package com.example.testriq.day_analyzer_module

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.testriq.R
import com.example.testriq.databinding.ActivityAllLocationBinding
import com.example.testriq.databinding.ActivityLocationBinding
import com.example.testriq.databinding.ActivityMapBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(),OnMapReadyCallback {

    lateinit var binding: ActivityMapBinding
    lateinit var client: FusedLocationProviderClient
    private lateinit var googleMap: GoogleMap
    private lateinit var mapView: MapView

    private var latitudeG = 0.0
    private var longitudeG = 0.0

    companion object {
        const val EXTRA_LATITUDE = "extra_latitude"
        const val EXTRA_LONGITUDE = "extra_longitude"
        const val TIME_MAP = "extra_time"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var latitude = intent.getDoubleExtra(LocationActivity.EXTRA_LATITUDE, 0.0)
        val longitude = intent.getDoubleExtra(LocationActivity.EXTRA_LONGITUDE, 0.0)
//        var latitude = intent.getDoubleExtra(LocationActivity.EXTRA_LATITUDE, 0.0)
//        val longitude = intent.getDoubleExtra(LocationActivity.EXTRA_LONGITUDE, 0.0)
        val timeMap = intent.getLongExtra(LocationActivity.TIME_MAP, 0L)
        Log.e("loc_latitude",latitude.toString())
        Log.e("loc_longitude",longitude.toString())
        Log.e("loc_time",timeMap.toString())

        latitudeG =latitude
        longitudeG=longitude
//        mapView = findViewById(R.id.mapView)
//        mapView.onCreate(savedInstanceState)
//        mapView.getMapAsync(this)

        val mapFragment: SupportMapFragment =supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googlemap: GoogleMap) {

        googleMap = googlemap

        val location = LatLng(latitudeG, longitudeG)
        googleMap.addMarker(MarkerOptions().position(location).title("Marker").icon(
            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
        ))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 12f))




    }
}