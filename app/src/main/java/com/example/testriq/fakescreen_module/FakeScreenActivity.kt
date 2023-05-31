package com.example.testriq.fakescreen_module

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.testriq.databinding.ActivityAutoClickCameraBinding
import com.example.testriq.databinding.ActivityFakeScreenBinding

class FakeScreenActivity : AppCompatActivity() {
    lateinit var binding: ActivityFakeScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFakeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}