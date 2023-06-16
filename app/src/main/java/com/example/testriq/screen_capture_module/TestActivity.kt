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
    val context=this
   var filePath=""
    private lateinit var mediaProjectionManager: MediaProjectionManager
    private var mediaProjection: MediaProjection? = null

    lateinit var binding: ActivityTestBinding
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
//
//        val intent = Intent(this, ScreenRecordService::class.java)
//        ContextCompat.startForegroundService(this, intent)
        val timestamp = LocalDateTime.now().toString().replace(":", "-")
        val fileName = "ak$timestamp-${Random().nextInt(10000)}.mp4"

        val folder = File(Environment.getExternalStorageDirectory(), "screen_record1")
        if (!folder.exists()) {
            folder.mkdirs()
        }
        val folderPath = folder.getAbsolutePath()
        filePath = File(folderPath, fileName).toString()


        // Initialize MediaProjectionManager


        mediaProjectionManager = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager

        // Initialize MediaRecorder
        mediaRecorder = MediaRecorder()
        mediaRecorder?.setVideoSource(MediaRecorder.VideoSource.SURFACE)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorder?.setVideoEncoder(MediaRecorder.VideoEncoder.H264)
        mediaRecorder?.setVideoEncodingBitRate(512 * 1000)
        mediaRecorder?.setVideoFrameRate(30)
        mediaRecorder?.setVideoSize(1280, 720)
        mediaRecorder?.setOutputFile(filePath)



        binding.btnStart.setOnClickListener {

            if (!isRecording) {
                startScreenRecording()
//                requestScreenCapture()
                binding.btnStart.text = "Stop Recording"
            } else {
                stopScreenRecording()
                binding.btnStart.text = "Start Recording"
            }

            Toast.makeText(this,"Start recording",Toast.LENGTH_LONG).show()
        }

//        binding.btnStop.setOnClickListener {
//            stopScreenRecording()
//            Toast.makeText(this,"Stop recording",Toast.LENGTH_LONG).show()
//        }
    }

    private fun startScreenRecording() {
        val mediaProjectionIntent = mediaProjectionManager.createScreenCaptureIntent()
        startActivityForResult(mediaProjectionIntent,11)
        Toast.makeText(this,"Start recording",Toast.LENGTH_LONG).show()
    }

    private fun stopScreenRecording() {
        mediaRecorder?.stop()
        mediaRecorder?.reset()
        mediaProjection?.stop()
        mediaProjection = null
        isRecording = false
    }






}