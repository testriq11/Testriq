package com.example.testriq.day_analyzer_module

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.room.Room
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.testriq.day_analyzer_module.db.LocationDatabase
import com.example.testriq.day_analyzer_module.model.TraceLocation
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch




//class LocationWorker(
//    private val context: Context,
//    workerParams: WorkerParameters
//) : Worker(context, workerParams) {
//
//    @SuppressLint("MissingPermission")
//    override fun doWork(): Result {
//        // Retrieve the location data from the input
//        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)
//
//        fusedLocationClient.lastLocation
//            .addOnSuccessListener {
//                    location: Location? ->
//                location?.let {
//                    val database = Room.databaseBuilder(
//                        applicationContext,
//                        LocationDatabase::class.java,
//                        "location-db"
//                    ).build()
//                    val locationDao = LocationDatabase.getDatabase(context).getTasksDao()
//                    val currentTimeMillis = System.currentTimeMillis()
//
//
//                    // Save the location to the database using the Room DAO
//
//                    val location = TraceLocation(
//                        latitude = location.latitude,
//                        longitude = location.longitude,
//                        timestamp = currentTimeMillis
//                    )
//                    GlobalScope.launch {
//                        locationDao.insert(location)
//                    }
//
//
//                }
//
//    }
//        return Result.success()
//        }   companion object {
//        const val KEY_LATITUDE = "latitude"
//        const val KEY_LONGITUDE = "longitude"
//    }
//    }



//}


class LocationWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    @SuppressLint("MissingPermission")
    override fun doWork(): Result {
        Log.e("WorkerClass","It's Working")
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    val database = Room.databaseBuilder(applicationContext, LocationDatabase::class.java, "location-db").build()
                    val locationDao = database.getTasksDao()
                    val currentTimeMillis = System.currentTimeMillis()

                    val locationData = TraceLocation(
                        latitude = location.latitude,
                        longitude = location.longitude,
                        timestamp = currentTimeMillis
                    )
                    Toast.makeText(applicationContext,"Data Inserted ",Toast.LENGTH_LONG).show()
                    GlobalScope.launch {
                    locationDao.insert(locationData)

                    }
                    database.close()

            }
       }
        return Result.success()
    }

}
