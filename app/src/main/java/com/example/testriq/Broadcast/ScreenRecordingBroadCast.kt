package com.example.testriq.Broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaRecorder
import android.util.Log
import android.widget.Toast
import com.example.testriq.screen_capture_module.MediaProjectionService
import com.example.testriq.screen_capture_module.ScreenRecordService

import java.util.*

class ScreenRecordingBroadCast :BroadcastReceiver() {

    private var mediaRecorder: MediaRecorder? = null
    private var isRecording = false
    private var startTime: Long = 0
    private var endTime: Long = 0
    var audioBroadCast:AudioBroadCast?= null

    private lateinit var filePath: String

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e("Alaram hit","Alaram hit")
        Toast.makeText(context, "Alarm triggered!", Toast.LENGTH_SHORT).show()

        val datetimeEnd= Date() // Replace this with your actual Date object
        val timeInMillis: Long = datetimeEnd.time
        endTime=timeInMillis

        val action = intent?.action

        if (action == ScreenRecordingBroadCast.START_SCREEN_RECORDING_ACTION) {
            // Start recording audio
            val serviceIntent = ScreenRecordService.newStartIntent(context!!)
            context.startService(serviceIntent)
            Log.e("if start record ","if condition start record")
            Toast.makeText(context,"Recording Started",Toast.LENGTH_LONG).show()

        } else {
            val serviceIntent = ScreenRecordService.newStopIntent(context!!)
            context.stopService(serviceIntent)

            Toast.makeText(context,"Recording Stopped through Service intent",Toast.LENGTH_LONG).show()
            Log.e("else Recording Stopped through Service intent ","else Recording Stopped through Service intent")
        }
    }

    companion object {
        const val START_SCREEN_RECORDING_ACTION = "com.example.testriq.START_SCREEN_RECORDING"
        const val STOP_SCREEN_RECORDING_ACTION = "com.example.testriq.STOP_SCREEN_RECORDING"
    }

}