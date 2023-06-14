package com.example.testriq.day_analyzer_module

import com.google.android.gms.maps.GoogleMap

interface MapReadyCallback {
    fun onMapReady(map: GoogleMap)
}