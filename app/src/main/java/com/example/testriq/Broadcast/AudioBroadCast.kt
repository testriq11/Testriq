package com.example.testriq.Broadcast

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.testriq.audio_module.AudioRecordedSave
import com.example.testriq.audio_module.AudioRecording
import com.example.testriq.audio_module.AudioRecordingService
import com.example.testriq.camera_module.AutoClickCamera
import java.io.File
import java.io.IOException
import java.time.LocalDateTime
import java.util.*

class AudioBroadCast : BroadcastReceiver(){
    private var mediaRecorder: MediaRecorder? = null
    private var isRecording = false
    private var startTime: Long = 0
    private var endTime: Long = 0
     var audioBroadCast:AudioBroadCast?= null

    private lateinit var filePath: String
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        val appContext = context?.applicationContext
//        val openIntent = Intent(context,AudioRecordedSave::class.java)
//        openIntent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//        context?.startActivity(openIntent)
        Log.e("Alaram hit","Alaram hit")
        Toast.makeText(context, "Alarm triggered!", Toast.LENGTH_SHORT).show()
//        val dateTimeEnd=intent?.getStringExtra("datetimeEnd")
//        val datetimeEnd = intent?.getLongExtra("datetimeEnd", 0)
        val datetimeEnd= Date() // Replace this with your actual Date object
        val timeInMillis: Long = datetimeEnd.time
//        val datetimeEnd: Long = yourDateObject.time

        endTime=timeInMillis

//        val action = intent?.action
//        if (action == "START_RECORDING") {
//            startRecordingService(context)
//        } else if (action == "STOP_RECORDING") {
//            stopRecordingService(context)
//        }
//
//        val action = intent.action
//
//        if (action != null && action == START_RECORDING) {
//            val serviceIntent = AudioRecorderService.newStartIntent(context)
//            context.startService(serviceIntent)
//        } else {
//            val serviceIntent = AudioRecorderService.newStopIntent(context)
//            context.startService(serviceIntent)
//        }


        val action = intent?.action

        if (action == START_RECORDING_ACTION) {
            // Start recording audio
            val serviceIntent = AudioRecordingService.newStartIntent(context!!)
            context.startService(serviceIntent)
            Log.e("if start record ","if condition start record")
            Toast.makeText(context,"Recording Started",Toast.LENGTH_LONG).show()
//            startRecordingService(context)
        } else {
//            val serviceIntent = Intent(context, AudioRecordingService::class.java)
//            context!!.stopService(serviceIntent)
            val serviceIntent = AudioRecordingService.newStopIntent(context!!)
            context.stopService(serviceIntent)
            // Stop recording audio
//            stopRecordingService(context)

//            context?.unregisterReceiver(this)
//            context?.unregisterReceiver(audioBroadCast)
            Toast.makeText(context,"Recording Stopped through Service intent",Toast.LENGTH_LONG).show()
            Log.e("else Recording Stopped through Service intent ","else Recording Stopped through Service intent")

        }

//        if (isRecording==false){
//            Log.e("if start record ","if condition start record")
//            startRecording()
//        }
//        else if (isRecording==true){
//                        stopRecording()
//            Log.e("else stop record ","else condition stop record")
//        }
//        startRecording()
//        Log.e("if start record onrecieve direct ","if start record onrecieve direct")
//        stopRecording()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun startRecordingService(context: Context?) {
        val serviceIntent = Intent(context, AudioRecordingService::class.java)
        ContextCompat.startForegroundService(context!!, serviceIntent)
    }

    private fun stopRecordingService(context: Context?) {
        val serviceIntent = Intent(context, AudioRecordingService::class.java)
        context?.stopService(serviceIntent)
    }
//    private fun startRecording() {
////        filePath = Environment.getExternalStorageDirectory().absolutePath + "/" + title + ".3gp"
//
//
//        val timestamp = LocalDateTime.now().toString().replace(":", "-")
//        val fileName = "audio$timestamp-${Random().nextInt(10000)}.mp3"
//
//        val folder = File(Environment.getExternalStorageDirectory(), "voice_record")
//        if (!folder.exists()) {
//            folder.mkdirs()
//        }
//        val folderPath = folder.getAbsolutePath()
//        filePath = File(folderPath, fileName).toString()
//        mediaRecorder = MediaRecorder()
//        mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
//        mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
//        mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
//        mediaRecorder!!.setOutputFile(filePath)
//        try {
//            mediaRecorder!!.prepare()
//            mediaRecorder!!.start()
//            startTime = System.currentTimeMillis()
//            Log.d("AudioRecorder", "Start Time: $startTime")
//        } catch (e: IOException) {
//            e.printStackTrace()
//        } catch (e: IllegalStateException) {
//            e.printStackTrace()
//        }
//    }
//    private fun startRecording(context: Context) {
//        mediaRecorder = MediaRecorder()
//        mediaRecorder?.apply {
//            setAudioSource(MediaRecorder.AudioSource.MIC)
//            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
//            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
//
//            val filePath = getOutputFilePath(context)
//            setOutputFile(filePath)
//
//            try {
//                prepare()
//                start()
//                isRecording = true
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }
//    }

//    private fun stopRecording() {
//        try {
//            Log.d("AudioRecorder endtime1", "End Time: $endTime")
//            mediaRecorder?.stop()
//            mediaRecorder = null
//
//            Log.d("AudioRecorder endtime2", "End Time: $endTime")
//        } catch (e: IOException) {
//            e.printStackTrace()
//        } catch (e: IllegalStateException) {
//            e.printStackTrace()
//        }
//
////        mediaRecorder?.apply {
////            Log.d("AudioRecorder endtime1", "End Time: $endTime")
////            stop()
////            Log.d("AudioRecorder endtime2", "End Time: $endTime")
//////            reset()
//////            release()
////             endTime = System.currentTimeMillis()
////            val duration = endTime - startTime
//////            Log.d("AudioRecorder", "Start Time: $startTime")
////            Log.d("AudioRecorder", "End Time: $endTime")
////            Log.d("AudioRecorder", "Duration: $duration milliseconds")
////        }
////        isRecording = false
//    }
//    private fun finishActivity(context: Context?) {
//        // Finish the activity using the context
////        if (context is Activity) {
////            context.finish()
////        }
//
//        val intent = Intent(context, AudioRecording::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        context?.startActivity(intent)
//    }

    companion object {
        const val START_RECORDING_ACTION = "com.example.testriq.START_RECORDING"
        const val STOP_RECORDING_ACTION = "com.example.testriq.STOP_RECORDING"
    }

}