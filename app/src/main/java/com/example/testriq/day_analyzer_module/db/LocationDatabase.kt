package com.example.testriq.day_analyzer_module.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.testriq.day_analyzer_module.dao.TraceLocationDao
import com.example.testriq.day_analyzer_module.model.TraceLocation

@Database(entities = [TraceLocation::class], version = 1, exportSchema = false)
abstract class LocationDatabase : RoomDatabase() {

    abstract fun getTasksDao(): TraceLocationDao

    companion object {
        // Volatile annotation means any change to this field
        // are immediately visible to other threads.
        @Volatile
        private var INSTANCE: LocationDatabase? = null

        private const val DB_NAME = "loc_database.db"

        fun getDatabase(context: Context): LocationDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            // here synchronised used for blocking the other thread
            // from accessing another while in a specific code execution.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocationDatabase::class.java,
                    DB_NAME
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}