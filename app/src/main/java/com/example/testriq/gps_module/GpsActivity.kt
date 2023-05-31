package com.example.testriq.gps_module

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.testriq.databinding.ActivityCameraBinding
import com.example.testriq.databinding.ActivityGpsBinding

class GpsActivity : AppCompatActivity() {
    lateinit var binding:ActivityGpsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityGpsBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)


    }
}