package com.example.testriq.day_analyzer_module

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.testriq.databinding.ActivityDayAnalyzerBinding
import com.example.testriq.databinding.ActivityDeleteTracesBinding

class DayAnalyzerActivity : AppCompatActivity() {
    lateinit var binding:ActivityDayAnalyzerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDayAnalyzerBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}