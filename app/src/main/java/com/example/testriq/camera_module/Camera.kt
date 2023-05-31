package com.example.testriq.camera_module

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.testriq.R
import com.example.testriq.Broadcast.CameraAlarmReceiver
import com.example.testriq.databinding.ActivityCameraBinding

class Camera  : AppCompatActivity() {
    private lateinit var timeEditText: EditText
    private lateinit var countdownTextView: TextView
    private lateinit var startButton: Button
    private var countdownTimer: CountDownTimer? = null
    private lateinit var binding: ActivityCameraBinding
    private var ALARAM_REQ_CODE :Int = 100
    private var REQUEST_PERMISSIONS_CODE=10
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCameraBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager



        requestPermissions()
//        binding.btnClick.setOnClickListener {
//
//            // time count down for 30 seconds,
//            // with 1 second as countDown interval
//
//            var time=Integer.parseInt(binding..text.toString())
//            var triggerTime:Long=System.currentTimeMillis()+(time*1000)
//
//
//            val currentTimeInMillis = System.currentTimeMillis()
//            var remainingTimeInMillis = triggerTime - currentTimeInMillis
//
//            countdownTimer?.cancel()
//            countdownTimer =  object : CountDownTimer(remainingTimeInMillis , 1000) {
//
//                // Callback function, fired on regular interval
//                override fun onTick(millisUntilFinished: Long) {
//                    val seconds = (millisUntilFinished / 1000) % 60
//                    val minutes = (millisUntilFinished / (1000 * 60)) % 60
//                    val hours = (millisUntilFinished / (1000 * 60 * 60)) % 24
//                    val countdownText = String.format(
//                        "%02d:%02d:%02d",
//                        hours,
//                        minutes,
//                        seconds
//                    )
//                    binding.textView.setText("seconds remaining: " +countdownText)
//                }
//
//                // Callback function, fired
//                // when the time is up
//                override fun onFinish() {
//                    binding.textView.setText("done!")
//                }
//            }  .start()
////            val intent = Intent(this, MainActivity::class.java)
////            intent.putExtra("Qty",binding.edtQtyImages.text.toString())
////            startActivity(intent)
////
//
//
////            val iBroadcast= Intent(this,CameraAlarmReceiver::class.java)
//            val iBroadcast= Intent(this, CameraAlarmReceiver::class.java)
//            val pi= PendingIntent.getBroadcast(this,ALARAM_REQ_CODE,iBroadcast,
//                PendingIntent.FLAG_IMMUTABLE)
//            alarmManager.set(AlarmManager.RTC_WAKEUP,triggerTime,pi)
//
//        }
    }

    private fun requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED||
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.READ_EXTERNAL_STORAGE,), REQUEST_PERMISSIONS_CODE)
        } else {
            // Permissions already granted
//            startCamera()
        }
    }
}
//class TimerActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityTimerBinding
//    private var ALARAM_REQ_CODE :Int = 100
//    private var REQUEST_PERMISSIONS_CODE=10
//    @RequiresApi(Build.VERSION_CODES.M)
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding=ActivityTimerBinding.inflate(layoutInflater)
//        val view=binding.root
//        setContentView(view)
//        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//
//
//
//        requestPermissions()
//        binding.btnClick.setOnClickListener {
//
//
//            var time=Integer.parseInt(binding.edtTime.text.toString())
//            var triggerTime:Long=System.currentTimeMillis()+(time*1000)
//
////            val intent = Intent(this, MainActivity::class.java)
////            intent.putExtra("Qty",binding.edtQtyImages.text.toString())
////            startActivity(intent)
////
//
//
////            val iBroadcast= Intent(this,CameraAlarmReceiver::class.java)
//            val iBroadcast= Intent(this,CameraAlarmReceiver::class.java)
//            val pi= PendingIntent.getBroadcast(this,ALARAM_REQ_CODE,iBroadcast,
//                PendingIntent.FLAG_IMMUTABLE)
//            alarmManager.set(AlarmManager.RTC_WAKEUP,triggerTime,pi)
//
//        }
//    }
//
//    private fun requestPermissions() {
//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
//            ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED||
//            ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//        ) {
//
//            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.READ_EXTERNAL_STORAGE,), REQUEST_PERMISSIONS_CODE)
//        } else {
//            // Permissions already granted
////            startCamera()
//        }
//    }
//}