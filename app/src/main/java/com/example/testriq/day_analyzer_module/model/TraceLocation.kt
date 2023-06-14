package com.example.testriq.day_analyzer_module.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trace_locations")
data class TraceLocation(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long

)

