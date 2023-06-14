package com.example.testriq.day_analyzer_module

import android.Manifest
import android.annotation.SuppressLint

import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.room.Room
import com.example.testriq.day_analyzer_module.dao.TraceLocationDao
import com.example.testriq.day_analyzer_module.db.LocationDatabase
import com.example.testriq.day_analyzer_module.model.TraceLocation
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import java.util.*
import kotlin.concurrent.schedule


class LocationService : Service(),OnMapReadyCallback {
    private var mapReadyCallback: MapReadyCallback? = null
    lateinit var client: FusedLocationProviderClient
    private lateinit var googleMap: GoogleMap
    private lateinit var locationDatabase: LocationDatabase
    private lateinit var locationDao: TraceLocationDao

    fun setMapReadyCallback(callback: MapReadyCallback) {
        mapReadyCallback = callback
    }
    override fun onCreate() {

    }

        override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//       onMapReady(googleMap)
//        return super.onStartCommand(intent, flags, startId)
////        fetchUserLocation(applicationContext)
//        return START_STICKY
            client = LocationServices.getFusedLocationProviderClient(this)
            val mapView = MapView(applicationContext)
            mapView.onCreate(null)
            mapView.getMapAsync(this)
            // ...
            return super.onStartCommand(intent, flags, startId)
    }

        @SuppressLint("MissingPermission")


    private fun fetchUserLocation(context: Context) {
//        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
//        fusedLocationClient.lastLocation
//            .addOnSuccessListener { location: Location? ->
//                location?.let {
//
//                    val latitude = it.latitude
//                    val longitude = it.longitude
//                    val timestamp = System.currentTimeMillis()
//
//                    val userLocation = TraceLocation(
//                        id =0,
//                        latitude = latitude, longitude = longitude, timestamp = timestamp)
//
//                    saveUserLocationToDatabase(context, userLocation)
//
//                    Toast.makeText(applicationContext,"Service Insert Data",Toast.LENGTH_LONG).show()
//                    Log.e("Service Insert Data","Service Insert Data")
//                }
//            }
//            val map: GoogleMap? = googlemap
//            Timer().schedule(0, 10000) {
//                client = LocationServices.getFusedLocationProviderClient(context)
//                client.lastLocation.addOnCompleteListener {
//                    locationDatabase = Room.databaseBuilder(
//                        applicationContext,
//                        LocationDatabase::class.java,
//                        "map_data"
//                    ).build()
//                    locationDao = locationDatabase.getTasksDao()
//                    val locationResult = it.result
//
//                    try {
//                        if (locationResult != null) {
//                            val latitude: Double = locationResult.latitude
//                            val longitude: Double = locationResult.longitude
//                            // Use the latitude value here
//
////                val latitude: Double = it.result.latitude
////                val longitude: Double = it.result.longitude
//                            val pos = LatLng(latitude, longitude)
////
////                            map?.addMarker(
////                                MarkerOptions().position(pos).title("My name").icon(
////                                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
////                                )
////                            )
////                            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 15f))
////                            Toast.makeText(
////                                context,
////                                "My Location: " + latitude.toString() + "," + longitude.toString(),
////                                Toast.LENGTH_LONG
////                            ).show()
//
//
//                            Log.e("lati>>", latitude.toString())
//
//                            Log.e("longi>>", longitude.toString())
//                            // Get the user's location using Google Maps API or location provider
//                            var eventID: Long = 0
//                            val timestamp = System.currentTimeMillis()
//                            val locationEntity = TraceLocation(eventID, latitude, longitude, timestamp)
//                            GlobalScope.launch(Dispatchers.IO) {
//                                locationDao.insert(locationEntity)
//                            }
//                        }
//                    }catch (e:java.lang.Exception){
//                        Log.e("Location is null","Location is null")
//                        Toast.makeText(applicationContext,"${e.message}",Toast.LENGTH_LONG).show()
//                    }
//
//
//                }
//
//            }
    }

    private fun saveUserLocationToDatabase(context: Context, userLocation: TraceLocation) {
        val database = LocationDatabase.getDatabase(context)
        CoroutineScope(Dispatchers.IO).launch {
            database.getTasksDao().insert(userLocation)
        }
    }
    override fun onDestroy() {
        // Clean up any resources
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    @SuppressLint("MissingPermission", "SuspiciousIndentation")
    override fun onMapReady(googlemap: GoogleMap) {
        val map: GoogleMap? = googlemap
        Timer().schedule(0, 10000) {
            client.lastLocation.addOnCompleteListener {
                locationDatabase = Room.databaseBuilder(
                    applicationContext,
                    LocationDatabase::class.java,
                    "map_data"
                ).build()
                locationDao = locationDatabase.getTasksDao()
                val locationResult = it.result

                    try {
                        if (locationResult != null) {
                        val latitude: Double = locationResult.latitude
                        val longitude: Double = locationResult.longitude
                        // Use the latitude value here

//                val latitude: Double = it.result.latitude
//                val longitude: Double = it.result.longitude
                        val pos = LatLng(latitude, longitude)

                        map?.addMarker(
                            MarkerOptions().position(pos).title("My name").icon(
                                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                            )
                        )
                        map?.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 15f))
                        Toast.makeText(
                            applicationContext,
                            "My Location: " + latitude.toString() + "," + longitude.toString(),
                            Toast.LENGTH_LONG
                        ).show()


                        Log.e("lati>>", latitude.toString())

                        Log.e("longi>>", longitude.toString())
                        // Get the user's location using Google Maps API or location provider
                        var eventID: Long = 0
                        val timestamp = System.currentTimeMillis()
                        val locationEntity = TraceLocation(eventID, latitude, longitude, timestamp)
                        GlobalScope.launch(Dispatchers.IO) {
                            locationDao.insert(locationEntity)
                        }
                    }
                }catch (e:java.lang.Exception){
                        Log.e("Location is null","Location is null")
                Toast.makeText(applicationContext,"${e.message}",Toast.LENGTH_LONG).show()
                }


            }

        }


    }
}



//class LocationService : Service() {
//    private lateinit var locationDatabase: AppDatabase
//    private lateinit var locationDao: TraceLocationDao
//    private lateinit var fusedLocationClient: FusedLocationProviderClient
//    private lateinit var locationRequest: LocationRequest
//    private lateinit var locationCallback: LocationCallback
//
//    override fun onCreate() {
//        super.onCreate()
//
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//        locationRequest = LocationRequest.create().apply {
//            interval = 10000 // 1 hour in milliseconds
//            fastestInterval = 10000
//            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        }
//
//        locationCallback = object : LocationCallback() {
//            override fun onLocationResult(locationResult: LocationResult) {
//                locationResult?.lastLocation?.let { location ->
//                    // Save the location to the room database
//                    saveLocationToDatabase(location.latitude, location.longitude)
//
//                    // Update the location on Google Maps
//                    updateLocationOnMap(location.latitude, location.longitude)
//                }
//            }
//        }
//
//        startLocationUpdates()
//    }
//
//    @SuppressLint("MissingPermission")
//    private fun startLocationUpdates() {
//        fusedLocationClient.requestLocationUpdates(
//            locationRequest,
//            locationCallback,
//            null
//        )
//    }
//
//    @SuppressLint("MissingPermission")
//    private fun saveLocationToDatabase(latitude: Double, longitude: Double) {
//        // Implement the logic to save the location to the room database here
//    var googlemap: GoogleMap? =null
//        val map:GoogleMap? =googlemap
//        fusedLocationClient.lastLocation.addOnCompleteListener {
////            locationDatabase = Room.databaseBuilder(
////                applicationContext,
////                AppDatabase::class.java,
////                "location"
////            ).build()
//            locationDatabase=
//            locationDao = locationDatabase.traceLocationDao()
//            val latitude:Double = it.result.latitude
//            val longitude:Double= it.result.longitude
//            val pos= LatLng(latitude,longitude)
//
//            map?.addMarker(MarkerOptions().position(pos).title("My name").icon(
//                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
//            ))
//            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 15f))
//            Toast.makeText(applicationContext,"My Location: " +latitude.toString()+ ","+longitude.toString(),Toast.LENGTH_LONG).show()
//
//
//            Log.e("lati>>",latitude.toString())
//
//            Log.e("longi>>",longitude.toString())
//            // Get the user's location using Google Maps API or location provider
//            var eventID:Long= 0
//            val timestamp = System.currentTimeMillis()
//            val locationEntity = TraceLocation(eventID,latitude,longitude,timestamp)
//            GlobalScope.launch(Dispatchers.IO) {
//                locationDao.insert(locationEntity)
//            }
//
//
//
//        }
//
//
//    }
//
//    private fun updateLocationOnMap(latitude: Double, longitude: Double) {
//        // Implement the logic to update the location on Google Maps here
//    }
//
//    override fun onBind(intent: Intent?): IBinder? {
//        return null
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        fusedLocationClient.removeLocationUpdates(locationCallback)
//    }
//}
