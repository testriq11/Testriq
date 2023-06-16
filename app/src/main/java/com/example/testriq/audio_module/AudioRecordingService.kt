package com.example.testriq.audio_module

import android.app.Notification
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.testriq.Broadcast.AudioBroadCast
import com.example.testriq.Broadcast.AudioBroadCast.Companion.START_RECORDING_ACTION
import com.example.testriq.Broadcast.AudioBroadCast.Companion.STOP_RECORDING_ACTION
import com.example.testriq.R
import java.io.File
import java.io.IOException
import java.time.LocalDateTime
import java.util.*

class AudioRecordingService : Service()  {
    private var mediaRecorder: MediaRecorder? = null
    private var isRecording = false
    private var startTime: Long = 0
    private var endTime: Long = 0
    var audioBroadCast: AudioBroadCast?= null
    private val NOTIFICATION_ID ="10001"


    private lateinit var filePath: String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        mediaRecorder = MediaRecorder()
//        // Create a notification for the foreground service
//        val notification = Notification.Builder(this, NOTIFICATION_ID)
//            .setContentTitle("Recording Service")
//            .setContentText("Recording in progress")
//            .setSmallIcon(R.drawable.ic_launcher_foreground)
//            .build()
//
//        // Start the service in the foreground
//        startForeground(1, notification)

        isRecording = true
        // Register the broadcast receiver to listen for stop recording action
        val filter = IntentFilter(STOP_RECORDING_ACTION)
        registerReceiver(stopRecordingReceiver, filter)
        // ...
    }




    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action

        if (action != null) {
            when (action) {
                START_RECORDING_ACTION -> {
//                    val outputDirectory = getOutputDirectory()
//                    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
//                    outputFile = "$outputDirectory/Recording_$timeStamp.3gp"
                    startRecording()
                    Log.e("Recording Service Started","Recording Service Started")
                }
                STOP_RECORDING_ACTION -> stopRecording()

            }

        }

        return START_STICKY

        // Start recording logic here
//        startRecording()

        // Return START_STICKY to ensure the service restarts automatically if it's killed by the system
//        return START_STICKY
    }

    private val stopRecordingReceiver = object : BroadcastReceiver() {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == STOP_RECORDING_ACTION) {
                stopRecording()
                stopSelf() // Stop the service after recording stops
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDestroy() {

        super.onDestroy()
        stopRecording()
        stopSelf()
        unregisterReceiver(stopRecordingReceiver)
        Log.e("Recording on destroy Service Stopped","Recording On Destroy Service Stopped")
//        if (isRecording) {
//            stopRecording()
//            Log.e("Recording on destroy Service Stopped","Recording On Destroy Service Stopped")
//        }
    }



    @RequiresApi(Build.VERSION_CODES.O)
     fun startRecording() {
//        filePath = Environment.getExternalStorageDirectory().absolutePath + "/" + title + ".3gp"


        val timestamp = LocalDateTime.now().toString().replace(":", "-")
        val fileName = "audio$timestamp-${Random().nextInt(10000)}.mp3"

        val folder = File(Environment.getExternalStorageDirectory(), "voice_record")
        if (!folder.exists()) {
            folder.mkdirs()
        }
        val folderPath = folder.getAbsolutePath()
        filePath = File(folderPath, fileName).toString()
        mediaRecorder = MediaRecorder()
        mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        mediaRecorder!!.setOutputFile(filePath)
        try {
            mediaRecorder!!.prepare()
            mediaRecorder!!.start()
            startTime = System.currentTimeMillis()
            Log.d("AudioRecorder", "Start Time: $startTime")
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun stopRecording() {
        try {
//            val datetimeEnd= Date() // Replace this with your actual Date object
//            val timeInMillis: Long = datetimeEnd.time
//            endTime=timeInMillis


            val localDateTime = LocalDateTime.now()
             endTime = localDateTime.toEpochSecond(java.time.ZoneOffset.UTC)
//            val datetimeEnd= Date()
//            val delayMillis =datetimeEnd.time+1000
//            endTime=delayMillis
//            if (delayMillis > 0) {
//                val handler = android.os.Handler()
//                handler.postDelayed({ stopRecording() }, delayMillis)
//            }
            Log.d("datetimeEnd", "End Time: $endTime")
            Log.d("AudioRecorder endtime1", "End Time: $endTime")
            mediaRecorder?.stop()
            mediaRecorder = null

            Log.d("AudioRecorder endtime2", "End Time: $endTime")
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            isRecording = false
        }

//        mediaRecorder?.apply {
//            Log.d("AudioRecorder endtime1", "End Time: $endTime")
//            stop()
//            Log.d("AudioRecorder endtime2", "End Time: $endTime")
////            reset()
////            release()
//             endTime = System.currentTimeMillis()
//            val duration = endTime - startTime
////            Log.d("AudioRecorder", "Start Time: $startTime")
//            Log.d("AudioRecorder", "End Time: $endTime")
//            Log.d("AudioRecorder", "Duration: $duration milliseconds")
//        }
//        isRecording = false
    }

    companion object {
//        const val START_RECORDING = "start_recording"
//        const val STOP_RECORDING = "stop_recording"

            fun newStartIntent(context: Context): Intent {
                return Intent(context, AudioRecordingService::class.java).apply {
                    action = START_RECORDING_ACTION
                }
            }

            fun newStopIntent(context: Context): Intent {
                return Intent(context, AudioRecordingService::class.java).apply {
                    action = STOP_RECORDING_ACTION
                }
            }
    }
}