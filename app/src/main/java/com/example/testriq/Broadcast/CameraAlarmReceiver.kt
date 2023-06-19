package com.example.testriq.Broadcast

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import com.example.testriq.MainActivity
import com.example.testriq.camera_module.AutoClickCamera
import com.example.testriq.day_analyzer_module.DayAnalyzerActivity

class CameraAlarmReceiver : BroadcastReceiver()  {
    companion object {
        const val ACTION_CLOSE_ACTIVITY = "com.example.testriq.ACTION_CLOSE_ACTIVITY"
    }
    override fun onReceive(context: Context?, intent: Intent?) {


        val myValue = intent?.getStringExtra("datetimeEnd")
        val myValueStart = intent?.getStringExtra("datetime")
//        val interval=intent?.getStringExtra("interval")
        val openIntent = Intent(context, AutoClickCamera::class.java)
        openIntent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        openIntent.putExtra("datetimeEnd",myValue)
        openIntent.putExtra("datetime",myValueStart)
//        openIntent.putExtra("interval",interval)
        context?.startActivity(openIntent)




    }
}