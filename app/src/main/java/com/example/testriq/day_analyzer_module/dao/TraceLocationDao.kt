package com.example.testriq.day_analyzer_module.dao

import androidx.room.*
import com.example.testriq.day_analyzer_module.model.TraceLocation

@Dao
interface TraceLocationDao {
    @Query("SELECT * FROM trace_locations")
    fun getAll(): List<TraceLocation>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(traceLocation: TraceLocation)

    @Delete
    fun delete(traceLocation: TraceLocation)
}

