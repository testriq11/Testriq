package com.example.testriq.screen_capture_module
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Environment
import android.os.IBinder
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.testriq.Broadcast.AudioBroadCast
import com.example.testriq.Broadcast.ScreenRecordingBroadCast
import com.example.testriq.R
import com.example.testriq.audio_module.AudioRecordingService
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class ScreenRecordService : Service() {
    private var mediaRecorder: MediaRecorder? = null
    private var isRecording = false

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (isRecording) {
            stopRecording()
        } else {
            startRecording()
        }
        return START_NOT_STICKY
    }

    private fun startRecording() {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val filePath = "${Environment.getExternalStorageDirectory().absolutePath}/$timestamp.mp4"

        mediaRecorder = MediaRecorder().apply {
            setVideoSource(MediaRecorder.VideoSource.SURFACE)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(filePath)
            setVideoEncoder(MediaRecorder.VideoEncoder.H264)
            setVideoEncodingBitRate(10000000)
            setVideoFrameRate(30)
            setVideoSize(1280, 720)

            try {
                prepare()
                start()
                isRecording = true
                Toast.makeText(this@ScreenRecordService, "Recording started", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun stopRecording() {
        mediaRecorder?.apply {
            try {
                mediaRecorder?.stop()
                mediaRecorder = null
                Toast.makeText(this@ScreenRecordService, "Recording stopped", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRecording()
    }

    companion object {
//        const val START_RECORDING = "start_recording"
//        const val STOP_RECORDING = "stop_recording"

        fun newStartIntent(context: Context): Intent {
            return Intent(context, AudioRecordingService::class.java).apply {
                action = AudioBroadCast.START_RECORDING_ACTION
            }
        }

        fun newStopIntent(context: Context): Intent {
            return Intent(context, AudioRecordingService::class.java).apply {
                action = AudioBroadCast.STOP_RECORDING_ACTION
            }
        }
    }
}