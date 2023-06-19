package com.example.testriq.camera_module

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.testriq.Broadcast.AudioBroadCast
import com.example.testriq.R
import com.example.testriq.Broadcast.CameraAlarmReceiver
import com.example.testriq.audio_module.AudioRecordingService
import com.example.testriq.databinding.ActivityCameraBinding
import java.text.SimpleDateFormat
import java.util.*

class Camera  : AppCompatActivity() {
    private lateinit var timeEditText: EditText
    private lateinit var countdownTextView: TextView
    private lateinit var startButton: Button
    private var countdownTimer: CountDownTimer? = null
    private lateinit var binding: ActivityCameraBinding
    private val imageCaptureInterval = 5000L
    private var REQUEST_PERMISSIONS_CODE = 10
    private lateinit var sharedPreferences: SharedPreferences
    val context =this
    private var ALARAM_REQ_CODE :Int = 200
    private var ALARAM_REQ_CODE_END :Int = 201


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager



        requestPermissions()

        binding.idEdtDate.setOnClickListener {
            showDateTimePicker()
        }
        binding.idEdtEndDate.setOnClickListener {
            showDateTimePickerEnd()
        }

        binding.btnClick.setOnClickListener {
            val dateTimeFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
            val selectedDateTime = binding.idEdtDate.text.toString()
            val selectedEndDateTime=binding.idEdtEndDate.text.toString()
            val date: Date = dateTimeFormat.parse(selectedDateTime)
            Log.e("date>>",date.toString())
            val endDate:Date = dateTimeFormat.parse(selectedEndDateTime)
            Log.e("endate>>",endDate.toString())

            sharedPreferences = getSharedPreferences("AlarmPrefs", Context.MODE_PRIVATE)

            val editor = sharedPreferences.edit()
            editor.putString("startTime", date.toString())
            editor.putString("endTime", endDate.toString())
            editor.apply()
            if (selectedDateTime.isNotEmpty() || selectedEndDateTime.isNotEmpty()  ) {
                setAlarm(context,date,endDate)
//                setAlarmEnd(context,endDate)
            }

            //            val intent = Intent(context, AudioRecordingService::class.java)
//            context.startForegroundService(intent)
            Toast.makeText(this,"Date & Time Scheduled",Toast.LENGTH_LONG).show()

        }
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
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                ),
                REQUEST_PERMISSIONS_CODE
            )
        } else {
            // Permissions already granted
//            startCamera()
        }
    }

    private val dateTimeFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())

    private fun showDateTimePicker() {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            this,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val timePickerDialog = TimePickerDialog(
                    this,
                    { _: TimePicker, hourOfDay: Int, minute: Int ->
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        calendar.set(Calendar.MINUTE, minute)

                        val selectedDateTime = dateTimeFormat.format(calendar.time)

                        binding.idEdtDate.setText(selectedDateTime)
                        Log.e("selectedDateTime>>", selectedDateTime)

//                        binding.idEdtEndDate.setText(selectedDateTime)
//                        Log.e("selectedEndDateTime>>",selectedDateTime)
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    false
                )
                timePickerDialog.show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }


    private fun showDateTimePickerEnd() {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            this,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val timePickerDialog = TimePickerDialog(
                    this,
                    { _: TimePicker, hourOfDay: Int, minute: Int ->
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        calendar.set(Calendar.MINUTE, minute)

                        val selectedDateTime = dateTimeFormat.format(calendar.time)
//                        val selectedDateTime = formatDate(calendar.time)

//                        binding.idEdtDate.setText(selectedDateTime)
//                        Log.e("selectedDateTime>>",selectedDateTime)

                        binding.idEdtEndDate.setText(selectedDateTime)
                        Log.e("selectedEndDateTime>>", selectedDateTime)
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    false
                )
                timePickerDialog.show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    @SuppressLint("WrongConstant")
    fun setAlarm(context: Context, dateTime: Date, dateTimeEnd: Date){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val startRecordingIntent = Intent(context, CameraAlarmReceiver::class.java).apply {
            action = "com.example.testriq.START_RECORDING"
            putExtra("datetime",dateTime.toString())
            putExtra("datetimeEnd",dateTimeEnd.toString())
        }
//        val startRecordingIntent = Intent(AudioBroadCast.START_RECORDING_ACTION)
//        sendBroadcast(startRecordingIntent)

//        val intent = Intent(context, AudioBroadCast::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, startRecordingIntent, ALARAM_REQ_CODE)
        alarmManager.set(AlarmManager.RTC_WAKEUP, dateTime.time, pendingIntent)
        Log.e(" dateTimeEnd.time", dateTime.time.toString() )

//        val stopRecordingIntent = Intent(context,  CameraAlarmReceiver::class.java).apply {
//            action = "com.example.testriq.STOP_RECORDING"
//
////            sendBroadcast(startRecordingIntent)
//        }
//        val stopRecordingIntent = Intent(AudioBroadCast.STOP_RECORDING_ACTION)
//        sendBroadcast(stopRecordingIntent)

//        val pendingIntentStop = PendingIntent.getBroadcast(context, 1,  stopRecordingIntent, ALARAM_REQ_CODE_END)
//        alarmManager.set(AlarmManager.RTC_WAKEUP, dateTimeEnd.time, pendingIntentStop)
//        Log.e(" dateTimeEnd.time", dateTimeEnd.time.toString() )

//
//        val stopRecordingIntentService = Intent(AudioBroadCast.STOP_RECORDING_ACTION)
//        sendBroadcast(stopRecordingIntentService )

//        val intent = Intent(this, AudioRecordingService::class.java)
//        intent.putExtra("datetimeEnd", dateTimeEnd)
//        startService(intent)
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