package com.example.testriq.day_analyzer_module

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.testriq.day_analyzer_module.dao.TraceLocationDao
import com.example.testriq.day_analyzer_module.model.TraceLocation

@Database(entities = [TraceLocation::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun traceLocationDao(): TraceLocationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "trace_locations_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
