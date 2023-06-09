package com.example.testriq.day_analyzer_module

import android.annotation.SuppressLint
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testriq.adapter.CallAdapter
import com.example.testriq.adapter.TraceLocationAdapter
import com.example.testriq.databinding.ActivityLocationBinding
import com.example.testriq.day_analyzer_module.model.TraceLocation
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class LocationActivity : AppCompatActivity() {
    val context=this
    private lateinit var googleApiClient: GoogleApiClient
    lateinit var binding: ActivityLocationBinding
    lateinit var database:AppDatabase
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationTracker: LocationTracker
    val locations: List<TraceLocation> = listOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val adapter = TraceLocationAdapter(locations)
        binding.rvLocations.layoutManager = LinearLayoutManager(this)
        binding.rvLocations.adapter = adapter
        saveTraceLocations(locations)

        setupGoogleApiClient()
    }

    private fun setupGoogleApiClient() {
        googleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
                override fun onConnected(bundle: Bundle?) {
                   Toast.makeText(context,"Google API Connected",Toast.LENGTH_LONG).show()
                }

                override fun onConnectionSuspended(i: Int) {
                    // Connection suspended
                    Toast.makeText(context,"Google API Not Connected",Toast.LENGTH_LONG).show()
                }
            })
            .addOnConnectionFailedListener { connectionResult ->
                // Connection failed
            }
            .addApi(LocationServices.API)
            .build()
    }

    override fun onStart() {
        super.onStart()
        googleApiClient.connect()
    }

    override fun onStop() {
        super.onStop()
        googleApiClient.disconnect()
    }


    @SuppressLint("MissingPermission")
    private fun fetchLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {

                    val latitude = location.latitude
                    val longitude = location.longitude
                    val timestamp=location.time
                    val locationData =TraceLocation(timestamp,latitude, longitude,)

                    // Add locationData to your RecyclerView adapter
                    // ...
                    val tracedLocations: MutableList<TraceLocation> = mutableListOf()
tracedLocations.add(TraceLocation(timestamp,latitude, longitude, ))
                    val adapter = TraceLocationAdapter(tracedLocations)
                    binding.rvLocations.layoutManager = LinearLayoutManager(this)
                    binding.rvLocations.adapter = adapter
                    saveTraceLocations(tracedLocations)
                }
            }
    }



    fun saveTraceLocations(locations: List<TraceLocation>) {
        val dao = AppDatabase.getInstance(context).traceLocationDao()
        for (location in locations) {
            dao.insert(location)
        }
    }


}