package com.example.testriq.screen_capture_module

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.testriq.databinding.ActivityScreenCaptureBinding

class ScreenCaptureActivity : AppCompatActivity() {

    lateinit var binding:ActivityScreenCaptureBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityScreenCaptureBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}