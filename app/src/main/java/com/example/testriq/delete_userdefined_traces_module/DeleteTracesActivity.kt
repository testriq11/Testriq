package com.example.testriq.delete_userdefined_traces_module

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.testriq.databinding.ActivityAutoClickCameraBinding
import com.example.testriq.databinding.ActivityDeleteTracesBinding

class DeleteTracesActivity : AppCompatActivity() {
    lateinit var binding: ActivityDeleteTracesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteTracesBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}