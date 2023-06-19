package com.example.testriq.screen_capture_module

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Environment
import android.os.IBinder
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.app.NotificationCompat
import com.example.testriq.Broadcast.AudioBroadCast
import com.example.testriq.R
import com.example.testriq.audio_module.AudioRecordingService
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class MediaProjectionService : Service() {

    private  val NOTIFICATION_ID = 1
    private  val CHANNEL_ID = "MediaProjectionServiceChannel"
    private lateinit var mediaProjectionManager: MediaProjectionManager
    private  var mediaRecorder: MediaRecorder? =null

    private var mediaProjection: MediaProjection? = null
    private var isRecording = false
var filePath=""
    override fun onCreate() {
        super.onCreate()
        mediaProjectionManager = getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        mediaRecorder = MediaRecorder()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
//        val notification = createNotification()
//        startForeground(1, notification)

        if (!isRecording) {
            val resultCode = intent.getIntExtra("resultCode", 0)
            val resultData = intent.getParcelableExtra<Intent>("resultData")
            mediaProjection =
                resultData?.let { mediaProjectionManager.getMediaProjection(resultCode, it) }
            startRecording()
        }
        createNotification()
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("MediaProjectionService")
            .setContentText("Foreground service is running")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Replace with your notification icon
            .build()

        startForeground(NOTIFICATION_ID, notification)

        return START_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startRecording() {

        val timestamp = LocalDateTime.now().toString().replace(":", "-")
        val fileName = "Sc$timestamp-${Random().nextInt(10000)}.mp4"

        val folder = File(Environment.getExternalStorageDirectory(), "SC_record")
        if (!folder.exists()) {
            folder.mkdirs()
        }

        val folderPath = folder.getAbsolutePath()
        filePath = File(folderPath, fileName).toString()
        var virtualDisplay = mediaProjection?.createVirtualDisplay(
            "Recording Display",
            screenWidth,
            screenHeight,
            screenDensity,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            mediaRecorder?.surface,
            null,
            null
        )

        mediaRecorder?.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setVideoSource(MediaRecorder.VideoSource.SURFACE)
            setOutputFormat(MediaRecorder.OutputFormat.WEBM)
            setOutputFile(filePath)
            setVideoSize(screenWidth, screenHeight)
            setVideoEncoder(MediaRecorder.VideoEncoder.VP8)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setVideoEncodingBitRate(512 * 1000)
            setVideoFrameRate(30)

            try {
                prepare()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            start()
            isRecording = true
        }
    }

    private fun stopRecording() {
        try {
            if ( mediaRecorder == null) {
                return
            }
            mediaRecorder?.apply {
                setOnErrorListener(null);
            setOnInfoListener(null);
                setPreviewDisplay(null);
                stop()

                mediaRecorder?.release()
            }
            var virtualDisplay = mediaProjection?.createVirtualDisplay(
                "Recording Display",
                screenWidth,
                screenHeight,
                screenDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mediaRecorder?.surface,
                null,
                null
            )

            if(virtualDisplay==null)
            {
                return
            }
            virtualDisplay.release()


            if (  mediaProjection != null) {
//                mediaProjection?.unregisterCallback()
                mediaProjection?.stop()
                mediaProjection = null
            }
//            mediaProjection?.stop()
            isRecording = false
        }
        catch (e: IllegalStateException)
        {
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification(){
        // Create and return a notification for the foreground service


        val channel = NotificationChannel(
            CHANNEL_ID,
            "MediaProjectionServiceChannel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.lightColor = Color.BLUE
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)

    }

    override fun onDestroy() {
        super.onDestroy()
        stopRecording()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    companion object {
        private const val screenWidth = 720
        private const val screenHeight = 1280
        private const val screenDensity = 320

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

//class MediaProjectionService : Service() {
//
//    companion object {
//        private const val NOTIFICATION_ID = 1
//        private const val CHANNEL_ID = "MediaProjectionServiceChannel"
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
//        createNotificationChannel()
//
//        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
//            .setContentTitle("MediaProjectionService")
//            .setContentText("Foreground service is running")
//            .setSmallIcon(R.drawable.ic_launcher_foreground) // Replace with your notification icon
//            .build()
//
//        startForeground(NOTIFICATION_ID, notification)
//
//        // Handle media projections logic here
//
//        return START_STICKY
//    }
//
//    override fun onBind(intent: Intent): IBinder? {
//        return null
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun createNotificationChannel() {
//        val channel = NotificationChannel(
//            CHANNEL_ID,
//            "MediaProjectionServiceChannel",
//            NotificationManager.IMPORTANCE_DEFAULT
//        )
//        channel.lightColor = Color.BLUE
//        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
//        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        manager.createNotificationChannel(channel)
//    }
//}