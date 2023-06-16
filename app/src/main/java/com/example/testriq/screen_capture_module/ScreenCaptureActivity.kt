package com.example.testriq.screen_capture_module

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import com.example.testriq.Broadcast.AudioBroadCast
import com.example.testriq.Broadcast.ScreenRecordingBroadCast
import com.example.testriq.audio_module.AudioRecordingService
import com.example.testriq.databinding.ActivityScreenCaptureBinding
import java.text.SimpleDateFormat
import java.util.*

class ScreenCaptureActivity : AppCompatActivity() {

    lateinit var binding:ActivityScreenCaptureBinding
    private lateinit var sharedPreferences: SharedPreferences
    val context =this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityScreenCaptureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.idEdtDate.setOnClickListener {
            showDateTimePicker()
        }
        binding.idEdtEndDate.setOnClickListener {
            showDateTimePickerEnd()
        }

        binding.switchAudio.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
//                deleteFolderFromInternalStorage()
            }
        }

        sharedPreferences = getSharedPreferences("AlarmPrefs", Context.MODE_PRIVATE)
        binding.saveSchedule.setOnClickListener {
            val dateTimeFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
            val selectedDateTime = binding.idEdtDate.text.toString()
            val selectedEndDateTime=binding.idEdtEndDate.text.toString()
            val date: Date = dateTimeFormat.parse(selectedDateTime)
            Log.e("date>>",date.toString())
            val endDate: Date = dateTimeFormat.parse(selectedEndDateTime)
            Log.e("endate>>",endDate.toString())


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
            Toast.makeText(this,"Date & Time Scheduled", Toast.LENGTH_LONG).show()

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
                        Log.e("selectedDateTime>>",selectedDateTime)

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
                        Log.e("selectedEndDateTime>>",selectedDateTime)
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

    fun setAlarm(context: Context, dateTime: Date,dateTimeEnd: Date){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val startRecordingIntent = Intent(context, ScreenRecordingBroadCast::class.java).apply {
            action = "com.example.testriq.START_RECORDING"
        }
//        val startRecordingIntent = Intent(AudioBroadCast.START_RECORDING_ACTION)
//        sendBroadcast(startRecordingIntent)

//        val intent = Intent(context, AudioBroadCast::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, startRecordingIntent,
            PendingIntent.FLAG_IMMUTABLE)
        alarmManager.set(AlarmManager.RTC_WAKEUP, dateTime.time, pendingIntent)
        Log.e(" dateTimeEnd.time", dateTime.time.toString() )

        val stopRecordingIntent = Intent(context, ScreenRecordingBroadCast::class.java).apply {
            action = "com.example.testriq.STOP_RECORDING"
            putExtra("datetimeEnd",dateTimeEnd)
        }
//        val stopRecordingIntent = Intent(AudioBroadCast.STOP_RECORDING_ACTION)
//        sendBroadcast(stopRecordingIntent)

        val pendingIntentStop = PendingIntent.getBroadcast(context, 1,  stopRecordingIntent,
            PendingIntent.FLAG_IMMUTABLE)
        alarmManager.set(AlarmManager.RTC_WAKEUP, dateTimeEnd.time, pendingIntentStop)
        Log.e(" dateTimeEnd.time", dateTimeEnd.time.toString() )


        val stopRecordingIntentService = Intent(ScreenRecordingBroadCast.STOP_RECORDING_ACTION)
        sendBroadcast(stopRecordingIntentService )

        val intent = Intent(this,ScreenRecordService::class.java)
        intent.putExtra("datetimeEnd", dateTimeEnd)
        startService(intent)
    }
}