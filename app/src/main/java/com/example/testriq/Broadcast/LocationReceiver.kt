package com.example.testriq.Broadcast

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import androidx.work.*
import com.example.testriq.day_analyzer_module.LocationService
import com.example.testriq.day_analyzer_module.LocationWorker
import com.example.testriq.day_analyzer_module.db.LocationDatabase
import com.example.testriq.day_analyzer_module.model.TraceLocation
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class LocationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Start your background work here
        val workRequest = OneTimeWorkRequestBuilder<LocationWorker>()
            .build()
        WorkManager.getInstance(context).enqueue(workRequest)
    }
}


//class LocationReceiver : BroadcastReceiver() {
//    override fun onReceive(context: Context?, intent: Intent?) {
//        val latitude = intent?.getDoubleExtra(KEY_LATITUDE, 0.0)
//        val longitude = intent?.getDoubleExtra(KEY_LONGITUDE, 0.0)
//
//        if (latitude != null && longitude != null) {
//            // Schedule the work using WorkManager
//            val workRequest = OneTimeWorkRequestBuilder<LocationWorker>()
//                .setInputData(workDataOf(
//                    LocationWorker.KEY_LATITUDE to latitude,
//                   LocationWorker.KEY_LONGITUDE to longitude
//                ))
//                .build()
//
//            WorkManager.getInstance(context!!).enqueue(workRequest)
//        }
//    }
//
//    companion object {
//        const val KEY_LATITUDE = "latitude"
//        const val KEY_LONGITUDE = "longitude"
//    }
//}


//class LocationReceiver : BroadcastReceiver() {
//    @SuppressLint("InvalidPeriodicWorkRequestInterval")
//    override fun onReceive(context: Context, intent: Intent) {
//        val constraints = Constraints.Builder()
//            .setRequiredNetworkType(NetworkType.CONNECTED)
//            .build()
//
//        val locationRequest = PeriodicWorkRequest.Builder(
//            LocationWorker::class.java,
//            10, TimeUnit.MILLISECONDS
//        ).setConstraints(constraints)
//            .build()
//
//        val workRequest = OneTimeWorkRequestBuilder<LocationWorker>().build()
//        WorkManager.getInstance(context).enqueue(workRequest)
//
////        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
////            "LocationWorker",
////            ExistingPeriodicWorkPolicy.REPLACE,
////            locationRequest
////        )
//    }
//}


//class LocationReceiver : BroadcastReceiver() {
//    override fun onReceive(context: Context?, intent: Intent?) {
//        // Start the service here
//        val serviceIntent = Intent(context, LocationService::class.java)
//        context?.startService(serviceIntent)
//    }
//}


//class LocationReceiver : BroadcastReceiver() {
//    override fun onReceive(context: Context, intent: Intent) {
//        if (intent.action == "ACTION_FETCH_LOCATION") {
//            fetchUserLocation(context)
//        }
//    }
//
//    @SuppressLint("MissingPermission")
//    private fun fetchUserLocation(context: Context) {
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
//                }
//            }
//    }
//
//    private fun saveUserLocationToDatabase(context: Context, userLocation: TraceLocation) {
//        val database = LocationDatabase.getDatabase(context)
//        CoroutineScope(Dispatchers.IO).launch {
//            database.getTasksDao().insert(userLocation)
//        }
//    }
//}