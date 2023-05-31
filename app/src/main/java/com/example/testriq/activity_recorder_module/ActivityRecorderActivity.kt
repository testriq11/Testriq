package com.example.testriq.activity_recorder_module

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.testriq.databinding.ActivityAudioRecordingBinding
import com.example.testriq.databinding.ActivityRecorderBinding

class ActivityRecorderActivity : AppCompatActivity() {

    lateinit var binding:ActivityRecorderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding= ActivityRecorderBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}