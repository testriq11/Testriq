package com.example.testriq.audio_module

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.testriq.Broadcast.AudioBoradCastEnd
import com.example.testriq.Broadcast.AudioBroadCast
import com.example.testriq.databinding.ActivityAudioRecordingBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AudioRecording : AppCompatActivity() {
    val context =this
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editText: EditText
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent
    private var ALARAM_REQ_CODE :Int = 100
    private var ALARAM_REQ_CODE_END :Int = 101
    private lateinit var binding: ActivityAudioRecordingBinding
    var timeInMillis:Long=0
    private lateinit var audioRecordingServiceIntent: Intent
    val audService= AudioRecordingService()
    @SuppressLint("WrongConstant")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioRecordingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        audioRecordingServiceIntent = Intent(this, AudioRecordingService::class.java)

//        val audioBroadcast = AudioBroadCast()
//        val intentFilter = IntentFilter(AudioBroadCast.STOP_RECORDING_ACTION)
//        registerReceiver(audioBroadcast, intentFilter)

//binding.saveSchedule.setOnClickListener {
//    binding.idEdtDate.addTextChangedListener(object : TextWatcher {
//        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//
//        override fun afterTextChanged(s: Editable?) {
//            // Update the alarm when the text changes
//
//            val dateTimeFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
//            val dateTime = dateTimeFormat.parse(s.toString())
////            dateTime?.let { setAlarm(applicationContext, it) }
//
//
//        }
//    })
//
//
//    Toast.makeText(this, "Date And Time Scheduled", Toast.LENGTH_LONG).show()
//}

        val dateTimeFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())

//        binding.idEdtDate.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//
//            override fun afterTextChanged(s: Editable?) {
//                // Update the alarm when the text changes
//                val dateTime = dateTimeFormat.parse(s.toString())
//                dateTime?.let { setAlarm(applicationContext, it) }
//            }
//        })

//        binding.idEdtEndDate.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//
//            override fun afterTextChanged(s: Editable?) {
//                // Update the alarm when the text changes
//                val dateTimeEnd = dateTimeFormat.parse(s.toString())
//                dateTimeEnd?.let { setAlarmEnd(applicationContext, it) }
//            }
//        })

        sharedPreferences = getSharedPreferences("AlarmPrefs", Context.MODE_PRIVATE)

        binding.idEdtDate.setOnClickListener {
            showDateTimePicker()
        }

        binding.saveSchedule.setOnClickListener {
            val dateTimeFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
            val selectedDateTime = binding.idEdtDate.text.toString()
            val selectedEndDateTime=binding.idEdtEndDate.text.toString()
            val date: Date = dateTimeFormat.parse(selectedDateTime)
            Log.e("date>>",date.toString())
            val endDate:Date = dateTimeFormat.parse(selectedEndDateTime)
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
            Toast.makeText(this,"Date & Time Scheduled",Toast.LENGTH_LONG).show()

        }

        binding.idEdtEndDate.setOnClickListener {
            showDateTimePickerEnd()
        }

        binding.switchAudio.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                deleteFolderFromInternalStorage()
            }
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

    @SuppressLint("WrongConstant")
//    fun setAlarm(context: Context, dateTime: Date) {
//        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//
//        val intent = Intent(context, AudioBroadCast::class.java)
//        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, ALARAM_REQ_CODE)
//        alarmManager.set(AlarmManager.RTC_WAKEUP, dateTime.time, pendingIntent)
//        val startRecordingIntent = Intent(AudioBroadCast.START_RECORDING_ACTION)
//        context.sendBroadcast(startRecordingIntent)
//
//        val stopRecordingIntent = Intent(AudioBroadCast.STOP_RECORDING_ACTION)
//        context.sendBroadcast(stopRecordingIntent)
//    }

    fun setAlarm(context: Context, dateTime: Date,dateTimeEnd: Date){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val startRecordingIntent = Intent(context, AudioBroadCast::class.java).apply {
            action = "com.example.testriq.START_RECORDING"
        }
//        val startRecordingIntent = Intent(AudioBroadCast.START_RECORDING_ACTION)
//        sendBroadcast(startRecordingIntent)

//        val intent = Intent(context, AudioBroadCast::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, startRecordingIntent, ALARAM_REQ_CODE)
        alarmManager.set(AlarmManager.RTC_WAKEUP, dateTime.time, pendingIntent)
        Log.e(" dateTimeEnd.time", dateTime.time.toString() )

        val stopRecordingIntent = Intent(context,  AudioBroadCast::class.java).apply {
            action = "com.example.testriq.STOP_RECORDING"
            putExtra("datetimeEnd",dateTimeEnd)
        }
//        val stopRecordingIntent = Intent(AudioBroadCast.STOP_RECORDING_ACTION)
//        sendBroadcast(stopRecordingIntent)

        val pendingIntentStop = PendingIntent.getBroadcast(context, 1,  stopRecordingIntent, ALARAM_REQ_CODE_END)
        alarmManager.set(AlarmManager.RTC_WAKEUP, dateTimeEnd.time, pendingIntentStop)
        Log.e(" dateTimeEnd.time", dateTimeEnd.time.toString() )


        val stopRecordingIntentService = Intent(AudioBroadCast.STOP_RECORDING_ACTION)
        sendBroadcast(stopRecordingIntentService )

        val intent = Intent(this, AudioRecordingService::class.java)
        intent.putExtra("datetimeEnd", dateTimeEnd)
        startService(intent)
    }

    @SuppressLint("WrongConstant")
    fun setAlarmEnd(context: Context, dateTimeEnd: Date) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val stopRecordingIntent = Intent(context,  AudioBoradCastEnd::class.java).apply {
            action = "com.example.testriq.STOP_RECORDING"
        }
//        val intent = Intent(context, AudioBroadCast::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0,  stopRecordingIntent, ALARAM_REQ_CODE_END)
        alarmManager.set(AlarmManager.RTC_WAKEUP, dateTimeEnd.time, pendingIntent)
//        val startRecordingIntent = Intent(AudioBroadCast.START_RECORDING_ACTION)
//        context.sendBroadcast(startRecordingIntent)
//
//        val stopRecordingIntent = Intent(AudioBroadCast.STOP_RECORDING_ACTION)
//        context.sendBroadcast(stopRecordingIntent)
    }

    private fun deleteFolderFromInternalStorage() {
        val folder = File(
            Environment.getExternalStorageDirectory().absolutePath, "voice_record"
        )
        if (folder.exists()) {
            val deleted = folder.deleteRecursively()
            if (deleted) {
                Toast.makeText(this,"Folder Deleted Succesfully",Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this,"Failed to  Deleted Succesfully",Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this,"Folder Doesn't Exist",Toast.LENGTH_LONG).show()
        }
    }


//
//    private fun showDateTimePicker() {
//        val calendar = Calendar.getInstance()
//
//        val datePicker = DatePickerDialog(
//            this,
//            DatePickerDialog.OnDateSetListener { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
//                calendar.set(year, month, dayOfMonth)
//                showTimePicker(calendar)
//            },
//            calendar.get(Calendar.YEAR),
//            calendar.get(Calendar.MONTH),
//            calendar.get(Calendar.DAY_OF_MONTH)
//        )
//
//        datePicker.datePicker.minDate = System.currentTimeMillis()
//        datePicker.show()
//    }
//
//    private fun showTimePicker(calendar: Calendar) {
//        val timePicker = TimePickerDialog(
//            this,
//            TimePickerDialog.OnTimeSetListener { _: TimePicker?, hourOfDay: Int, minute: Int ->
//                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
//                calendar.set(Calendar.MINUTE, minute)
//                updateTimeEditText(calendar.time)
//            },
//            calendar.get(Calendar.HOUR_OF_DAY),
//            calendar.get(Calendar.MINUTE),
//            false
//        )
//
//        timePicker.show()
//    }
//
//    private fun updateTimeEditText(date: Date) {
//        val format = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
//        val formattedDate = format.format(date)
//        binding.idEdtDate.setText(formattedDate)
//     setAlarm(timeInMillis)
//
//    }
//
//    private fun setAlarm(timeInMillis: Long) {
//        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val intent = Intent(this, AudioBroadCast::class.java)
//        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
//
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
//    }

//    private fun showTimePicker() {
//        val calendar = Calendar.getInstance()
//        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
//        val minute = calendar.get(Calendar.MINUTE)
//
//        val timePickerDialog = TimePickerDialog(
//            this,
//            TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
//                val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
//                binding.idEdtTime.setText(selectedTime)
//            },
//            hourOfDay,
//            minute,
//            true
//        )
//
//        timePickerDialog.show()
//    }

//    private fun showDatePicker() {
//        val calendar = Calendar.getInstance()
//        val year = calendar.get(Calendar.YEAR)
//        val month = calendar.get(Calendar.MONTH)
//        val day = calendar.get(Calendar.DAY_OF_MONTH)
//
//        val datePickerDialog = DatePickerDialog(
//            this,
//            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
//                val selectedDate = "$selectedDay-${selectedMonth + 1}-$selectedYear"
//                binding.idEdtDate.setText(selectedDate)
//            },
//            year,
//            month,
//            day
//        )
//
//        datePickerDialog.show()
//    }
//override fun onPause() {
//    super.onPause()
//    val intent = Intent(this, AudioRecordingService::class.java)
//    intent.action = AudioBroadCast.STOP_RECORDING_ACTION
//    startService(intent)
//}


//    override fun onPause() {
//        super.onPause()
//     audService.stopRecording()
//            val intent = Intent(this, AudioRecordingService::class.java)
//    intent.action = AudioBroadCast.STOP_RECORDING_ACTION
//    startService(intent)
//    }

    override fun onPause() {
        super.onPause()
        startService(audioRecordingServiceIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(audioRecordingServiceIntent)
    }

    override fun onResume() {
        super.onResume()
        stopService(audioRecordingServiceIntent)
    }


}
