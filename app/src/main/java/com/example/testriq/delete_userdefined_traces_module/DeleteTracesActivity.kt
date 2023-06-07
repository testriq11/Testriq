package com.example.testriq.delete_userdefined_traces_module

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.testriq.databinding.ActivityDeleteTracesBinding
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.auth.api.phone.SmsRetrieverClient
import java.text.SimpleDateFormat
import java.util.*

class DeleteTracesActivity : AppCompatActivity() {
    val context = this
    private val WHATSAPP_PROVIDER_AUTHORITY = "com.whatsapp.provider.WhatsAppDataProvider"
    private val WHATSAPP_MESSAGE_TABLE = "messages"
    lateinit var binding: ActivityDeleteTracesBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        val messageHelper = MessageHelper(applicationContext)
        messageHelper.deleteAllMessages()
        binding = ActivityDeleteTracesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.saveSchedule.setOnClickListener {

//                deleteWhatsAppMessages()
            val phoneNumber = "+919511807337"
//                val timestamp=System.currentTimeMillis() - (24 * 60 * 60 * 1000)
//
//                val dateTimeFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
//                val selectedDateTime = binding.idEdtDate.text.toString()
//                val selectedEndDateTime=binding.idEdtEndDate.text.toString()
//                val date: Date = dateTimeFormat.parse(selectedDateTime)
//                Log.e("date>>",date.toString())
//                val endDate:Date = dateTimeFormat.parse(selectedEndDateTime)
//                Log.e("endate>>",endDate.toString())
//                deleteWhatsAppMessages(contex,phoneNumber,timestamp)

//            val threadId = getSMSThreadId(this)
//   Log.e("t>>",threadId.toString())
            val client: SmsRetrieverClient = SmsRetriever.getClient(this)
            deleteWhatsAppChats(contentResolver)
             }

        binding.idEdtDate.setOnClickListener {
            showDateTimePicker()
        }
        binding.idEdtEndDate.setOnClickListener {
            showDateTimePickerEnd()
        }

    }
    fun deleteWhatsAppChats(contentResolver: ContentResolver) {
        // WhatsApp's content provider URI
        val whatsAppUri: Uri = Uri.parse("content://com.whatsapp.provider.media/item")

        // Build the selection clause to delete all chats
        val selection = "media_type = 0" // 0 represents chat messages

        // Delete the chats using the content resolver
        val rowsDeleted = contentResolver.delete(whatsAppUri, selection, null)

        if (rowsDeleted > 0) {
            // Chats deleted successfully
        } else {
            // No chats found or error occurred
        }
    }
    private fun deleteAllSMS() {
        val contentResolver: ContentResolver = contentResolver
        val smsUri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Telephony.Sms.CONTENT_URI
        } else {
            Uri.parse("content://sms")
        }

        // Delete all SMS messages
        contentResolver.delete(smsUri, null, null)

        Toast.makeText(this, "All SMS messages deleted.", Toast.LENGTH_SHORT).show()
    }

    private fun showDateTimePickerEnd() {
        val calendar = Calendar.getInstance()
        sharedPreferences = getSharedPreferences("AlarmPrefs", Context.MODE_PRIVATE)
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

    private fun deleteWhatsAppMessages() {

        val dateTimeFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
        val selectedDateTime = binding.idEdtDate.text.toString()
        val selectedEndDateTime = binding.idEdtEndDate.text.toString()
        val date: Date = dateTimeFormat.parse(selectedDateTime)
        Log.e("date>>", date.toString())
        val endDate: Date = dateTimeFormat.parse(selectedEndDateTime)
        Log.e("endate>>", endDate.toString())


        val editor = sharedPreferences.edit()
        editor.putString("startTime", date.toString())
        editor.putString("endTime", endDate.toString())
        editor.apply()

//                    setAlarm(context,date,endDate)
//                setAlarmEnd(context,endDate)
    }

    //            val intent = Intent(context, AudioRecordingService::class.java)
//            context.startForegroundService(intent)


//fun deleteWhatsAppMessages(context: Context, phoneNumber: String, timestamp:Long) {
//
//
//    val contentResolver = context.contentResolver
//
//    // Retrieve the message URI
//    val uri = Uri.parse("content://com.whatsapp.provider.media/item")
//
//    // Construct the selection and selection arguments
//    val selection = "key_remote_jid = ? AND timestamp <= ?"
//    val selectionArgs = arrayOf("$phoneNumber@s.whatsapp.net", "$timestamp")
//
//    // Delete the messages
//    val deletedCount = contentResolver.delete(uri, selection, selectionArgs)
//
//    // Optionally, you can check the deletedCount to verify if any messages were deleted
//    if (deletedCount > 0) {
//        // Messages were deleted successfully
//     Toast.makeText(context,"Deleted Reocord at set time", Toast.LENGTH_LONG).show()
//    }
//}}

    fun deleteAllSms() {
        val contentResolver: ContentResolver = context.contentResolver
        val smsUri: Uri = Telephony.Sms.CONTENT_URI

        // Delete all SMS messages
        val deleteCount = contentResolver.delete(smsUri, null, null)
        println("Deleted $deleteCount SMS messages")
    }


}
