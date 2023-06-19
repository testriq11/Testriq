package com.example.testriq.screen_capture_module

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.hardware.display.DisplayManager
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.testriq.databinding.ActivityScreenCaptureBinding
import com.example.testriq.databinding.ActivityTestBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class TestActivity : AppCompatActivity() {
    private var mediaRecorder: MediaRecorder? = null
    private var isRecording = false
    val context = this
    var filePath = ""
    private lateinit var mediaProjectionManager: MediaProjectionManager
    private var mediaProjection: MediaProjection? = null


    lateinit var binding: ActivityTestBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mediaProjectionManager =
            getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
//        val intent = Intent(this, ScreenRecordService::class.java)
//        ContextCompat.startForegroundService(this, intent)


        binding.btnStart.setOnClickListener {

            val serviceIntent = Intent(this, MediaProjectionService::class.java)
            startService(serviceIntent)

            Toast.makeText(this, "Start recording", Toast.LENGTH_LONG).show()
        }

        binding.btnStop.setOnClickListener {
            val serviceIntent1 = Intent(this, MediaProjectionService::class.java)
          stopService(serviceIntent1)
            Toast.makeText(this, "Stop recording", Toast.LENGTH_LONG).show()
        }
    }






}