package com.example.testriq.Broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaRecorder
import android.util.Log
import java.io.IOException

class AudioBoradCastEnd  : BroadcastReceiver() {
    private var mediaRecorder: MediaRecorder? = null
    private var isRecording = false
    private var startTime: Long = 0
    private var endTime: Long = 0
    private lateinit var filePath: String
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        if (action == STOP_RECORDING_ACTION ) {

            // Stop recording audio
            stopRecording()
            Log.e("else stop record ","else condition stop record")
        }
//        stopRecording()
//        Log.e("stop  onrecieve direct ","stop record onrecieve direct")
    }
    private fun stopRecording() {

        try {
            Log.d("AudioRecorder endtime1", "End Time: $endTime")
            mediaRecorder?.stop()
            Log.d("AudioRecorder endtime2", "End Time: $endTime")
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
//        mediaRecorder?.apply {
//            stop()
//            reset()
//            release()
//             endTime = System.currentTimeMillis()
//            val duration = endTime - startTime
//            Log.d("AudioRecorder", "Start Time: $startTime")
//            Log.d("AudioRecorder", "End Time: $endTime")
//            Log.d("AudioRecorder", "Duration: $duration milliseconds")
//        }
//        isRecording = false
    }
    companion object {
        const val START_RECORDING_ACTION = "com.example.testriq.START_RECORDING"
        const val STOP_RECORDING_ACTION = "com.example.testriq.STOP_RECORDING"
    }
}