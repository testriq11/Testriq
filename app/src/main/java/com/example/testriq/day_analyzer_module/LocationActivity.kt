package com.example.testriq.day_analyzer_module

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.room.Room
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.testriq.Broadcast.LocationReceiver
import com.example.testriq.R

import com.example.testriq.databinding.ActivityLocationBinding
import com.example.testriq.day_analyzer_module.dao.TraceLocationDao
import com.example.testriq.day_analyzer_module.db.LocationDatabase
import com.example.testriq.day_analyzer_module.model.TraceLocation
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule
class LocationActivity : AppCompatActivity(), MapReadyCallback {
//class LocationActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var database: LocationDatabase
    lateinit var client: FusedLocationProviderClient
    val context = this
    private lateinit var locationDatabase: LocationDatabase
    private lateinit var locationDao: TraceLocationDao

    lateinit var binding: ActivityLocationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
//
//        val intent=Intent(context,LocationService::class.java)
//        ContextCompat.startForegroundService(this,intent)
        // Initialize the Room database
//        database =
//            Room.databaseBuilder(applicationContext, LocationDatabase::class.java, "map_data.db")
//                .build()


        val intent = Intent(this, LocationService::class.java)
        startService(intent)
        val service = LocationService()
        service.setMapReadyCallback(context)
        service.onMapReady(googleMap)



        // Initialize the MapView
//        mapView = findViewById(R.id.mapView)
//        mapView.onCreate(savedInstanceState)
//        mapView.getMapAsync(this)
//        val mapFragment: SupportMapFragment =supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
//        mapFragment.getMapAsync(this)
//        client = LocationServices.getFusedLocationProviderClient(this)
//        mapView = findViewById(R.id.mapView)
//        mapView.onCreate(savedInstanceState)
//        mapView.getMapAsync(context)
//        binding.mapView
        // Schedule periodic map data saving
        Timer().schedule(0, 10000) {
//            saveMapDataToDatabase()
        }
    }

//    @SuppressLint("MissingPermission")
//    override fun onResume() {
//        super.onResume()
//        mapView.onResume()
//
//    }

    @SuppressLint("MissingPermission")
//    override fun onPause() {
//        super.onPause()
//        mapView.onPause()
//        Timer().schedule(0, 10000) {
//            client.lastLocation.addOnCompleteListener {
//                locationDatabase = Room.databaseBuilder(
//                    applicationContext,
//                    LocationDatabase::class.java,
//                    "map_data"
//                ).build()
//                locationDao = locationDatabase.getTasksDao()
//                val locationResult = it.result
//
//                try {
//                    if (locationResult != null) {
//                        val latitude: Double = locationResult.latitude
//                        val longitude: Double = locationResult.longitude
//                        // Use the latitude value here
//
////                val latitude: Double = it.result.latitude
////                val longitude: Double = it.result.longitude
//                        val pos = LatLng(latitude, longitude)
//
////                        map?.addMarker(
////                            MarkerOptions().position(pos).title("My name").icon(
////                                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
////                            )
////                        )
////                        map?.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 15f))
////                        Toast.makeText(
////                            context,
////                            "My Location: " + latitude.toString() + "," + longitude.toString(),
////                            Toast.LENGTH_LONG
////                        ).show()
//
//
//                        Log.e("lati>>", latitude.toString())
//
//                        Log.e("longi>>", longitude.toString())
//                        // Get the user's location using Google Maps API or location provider
//                        var eventID: Long = 0
//                        val timestamp = System.currentTimeMillis()
//                        val locationEntity = TraceLocation(eventID, latitude, longitude, timestamp)
//                        GlobalScope.launch(Dispatchers.IO) {
//                            locationDao.insert(locationEntity)
//                        }
//                    }
//                }catch (e:java.lang.Exception){
//                    Log.e("Location is null","Location is null")
//                    Toast.makeText(applicationContext,"${e.message}",Toast.LENGTH_LONG).show()
//                }
//
//
//            }
//
//        }
//    }

//    override fun onDestroy() {
//        super.onDestroy()
//        mapView.onDestroy()
//    }

//    override fun onLowMemory() {
//        super.onLowMemory()
//        mapView.onLowMemory()
//    }

    override fun onMapReady(googlemap: GoogleMap) {

        // Perform any map-related operations here
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
                            context,
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
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
//        mapView.onSaveInstanceState(outState)
    }

//    override fun onMapReady(map: GoogleMap) {
//        TODO("Not yet implemented")
//    }
}

//    @SuppressLint("MissingPermission")
//    private fun saveMapDataToDatabase() {
//        // Retrieve the necessary map data from the GoogleMap instance
//        // Save the data to the Room database using database operations
//
//        client.lastLocation.addOnCompleteListener {
//            locationDatabase = Room.databaseBuilder(
//                applicationContext,
//                LocationDatabase::class.java,
//                "location-db"
//            ).build()
//            locationDao = locationDatabase.getTasksDao()
//try {
//    val latitude: Double = it.result.latitude
//    val longitude: Double = it.result.longitude
////            val pos= LatLng(latitude,longitude)
//
//    var eventID: Long = 0
//    val timestamp = System.currentTimeMillis()
//    val locationEntity = TraceLocation(eventID, latitude, longitude, timestamp)
//    GlobalScope.launch(Dispatchers.IO) {
//        locationDao.insert(locationEntity)
//    }
//}catch (e: Exception){
//    Toast.makeText(applicationContext,"${e.message}",Toast.LENGTH_LONG).show()
//}
////            else{
////                Log.e("Location is null","Location is null")
////                Toast.makeText(applicationContext,"Location is null",Toast.LENGTH_LONG).show()
////            }
//    }
//}}


//class LocationActivity : AppCompatActivity() {
////class LocationActivity : AppCompatActivity() , OnMapReadyCallback {
////class LocationActivity : AppCompatActivity(),FragmentCallback{
//private lateinit var alarmManager: AlarmManager
//    private lateinit var pendingIntent: PendingIntent
//    private lateinit var myService: LocationService
//    lateinit var client: FusedLocationProviderClient
//    val context=this
//    private lateinit var locationDatabase: LocationDatabase
//    private lateinit var locationDao: TraceLocationDao
//    private lateinit var googleApiClient: GoogleApiClient
//    lateinit var binding: ActivityLocationBinding
//    lateinit var database: LocationDatabase
////    private lateinit var fusedLocationClient: FusedLocationProviderClient
//    private lateinit var locationTracker: LocationTracker
//    val locations: List<TraceLocation> = listOf()
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityLocationBinding.inflate(layoutInflater)
//        setContentView(binding.root)
////        val intent1 = Intent(this, LocationService::class.java)
////        startService(intent1)
//
//        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val intent = Intent(this, LocationReceiver::class.java)
//        intent.action = "ACTION_FETCH_LOCATION"
//        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
//
//        scheduleLocationUpdates()
//
////        workmanger()
//
//
////        val mapFragment: SupportMapFragment =supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
////        mapFragment?.getMapAsync(context)
////        client = LocationServices.getFusedLocationProviderClient(this)
////        myService = LocationService()
////        myService.setFragmentCallback(this)
////        startService(Intent(this, LocationService::class.java))
//
//
//    }
//
////    private fun workmanger() {
////        val latitude = intent?.getDoubleExtra(LocationReceiver.KEY_LATITUDE, 0.0)
////        val longitude = intent?.getDoubleExtra(LocationReceiver.KEY_LONGITUDE, 0.0)
////        val workRequest = OneTimeWorkRequestBuilder<LocationWorker>()
////            .setInputData(workDataOf(
////                LocationWorker.KEY_LATITUDE to latitude,
////                LocationWorker.KEY_LONGITUDE to longitude
////            ))
////            .build()
////
////        WorkManager.getInstance(context).enqueue(workRequest)
////    }
//
//    private fun scheduleLocationUpdates() {
////        val intervalMillis = 10000// 10sec
////
////        val triggerAtMillis = SystemClock.elapsedRealtime() + intervalMillis
//
//        val intervalMillis = 10000 // 10 sec
//        val initialDelayMillis = 0 // 0 milliseconds
//
//        alarmManager.setRepeating(
//            AlarmManager.RTC_WAKEUP,
//            System.currentTimeMillis() + initialDelayMillis,
//            intervalMillis.toLong(),
//            pendingIntent
//        )
//    }
//
//    override fun onPause() {
//        super.onPause()
//        val intervalMillis = 10000 // 10 sec
//        val initialDelayMillis = 0 // 0 milliseconds
//
//        alarmManager.setRepeating(
//            AlarmManager.RTC_WAKEUP,
//            System.currentTimeMillis() + initialDelayMillis,
//            intervalMillis.toLong(),
//            pendingIntent
//        )
//    }
//
//    override fun onStop() {
//        super.onStop()
//
//        val intervalMillis = 10000 // 10 sec
//        val initialDelayMillis = 0 // 0 milliseconds
//
//        alarmManager.setRepeating(
//            AlarmManager.RTC_WAKEUP,
//            System.currentTimeMillis() + initialDelayMillis,
//            intervalMillis.toLong(),
//            pendingIntent
//        )
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//
//        // Cancel the alarm when the activity is destroyed
//        alarmManager.cancel(pendingIntent)
//    }
//
//
//
//
//
////    @SuppressLint("MissingPermission")
////    override fun onMapReady(googlemap: GoogleMap) {
////        val map:GoogleMap? =googlemap
////           client.lastLocation.addOnCompleteListener {
////               locationDatabase = Room.databaseBuilder(
////                   applicationContext,
////                   LocationDatabase::class.java,
////                   "location-database"
////               ).build()
////               locationDao = locationDatabase.getTasksDao()
////                val latitude:Double = it.result.latitude
////                val longitude:Double= it.result.longitude
////                val pos= LatLng(latitude,longitude)
////
////                map?.addMarker(MarkerOptions().position(pos).title("My name").icon(
////                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
////                ))
////               map?.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 15f))
////                Toast.makeText(context,"My Location: " +latitude.toString()+ ","+longitude.toString(),Toast.LENGTH_LONG).show()
////
////
////               Log.e("lati>>",latitude.toString())
////
////               Log.e("longi>>",longitude.toString())
////               // Get the user's location using Google Maps API or location provider
////               var eventID:Long= 0
////               val timestamp = System.currentTimeMillis()
////               val locationEntity = TraceLocation(eventID,latitude,longitude,timestamp)
////               GlobalScope.launch(Dispatchers.IO) {
////                   locationDao.insert(locationEntity)
////               }
////
////
////
////            }
////
////
////    }
//
//
//
//}

//private fun Timer.scheduleAtFixedRate(timerTask: TimerTask, interval: Int, interval1: Int) {
//
//}
