package com.example.testriq.Broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.testriq.camera_module.AutoClickCamera

class CameraAlarmReceiver : BroadcastReceiver()  {
    override fun onReceive(context: Context?, intent: Intent?) {

        val openIntent = Intent(context, AutoClickCamera::class.java)
        openIntent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context?.startActivity(openIntent)


    }
}