package com.example.testriq.day_analyzer_module

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CallLog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testriq.adapter.CallAdapter
import com.example.testriq.adapter.SMSAdapter
import com.example.testriq.databinding.ActivityCallBinding
import com.example.testriq.databinding.ActivityMessageBinding
import com.example.testriq.day_analyzer_module.model.Call
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class CallActivity : AppCompatActivity() {
    lateinit var binding: ActivityCallBinding
    private lateinit var adapter: CallAdapter
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCallBinding.inflate(layoutInflater)
        setContentView(binding.root)

      adapter = CallAdapter()
        binding.btnDatePicker.setOnClickListener {
            showDatePicker()
        }
        binding.rvCalls.layoutManager = LinearLayoutManager(this)
        binding.rvCalls.adapter = adapter
    }

    private fun fetchCallLogs(startDate: Date, endDate: Date) {
        val callList = mutableListOf<Call>()

        val projection = arrayOf(
            CallLog.Calls.NUMBER,
            CallLog.Calls.DATE,
            CallLog.Calls.DURATION
        )

        val selection = "${CallLog.Calls.DATE} >= ? AND ${CallLog.Calls.DATE} - ${1}<= ?"
        val selectionArgs = arrayOf(startDate.time.toString(), endDate.time.toString())
        val sortOrder = "${CallLog.Calls.DATE} DESC"

        val cursor = contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )

        cursor?.use {
            val numberColumn = it.getColumnIndex(CallLog.Calls.NUMBER)
            val dateColumn = it.getColumnIndex(CallLog.Calls.DATE)
            val durationIndex = it.getColumnIndex(CallLog.Calls.DURATION)
//            val typeIndex = it.getColumnIndex(CallLog.Calls.TYPE)
            while (it.moveToNext()) {
                val number = it.getString(numberColumn)
//                val date = it.getString(dateColumn)
                val dateInMillis= cursor.getLong(dateColumn)
                val date = Date(dateInMillis)

                val duration = cursor.getLong(durationIndex)

//                val callType = getCallType(cursor.getInt(typeIndex))

// Set the formatted duration to your UI element

                val call = Call(number, date,duration)

                callList.add(call)
            }
        }

        adapter.setData(callList)
    }

    private fun getCallType(callTypeCode: Int): String {
        return when (callTypeCode) {
            CallLog.Calls.INCOMING_TYPE -> "Incoming"
            CallLog.Calls.OUTGOING_TYPE -> "Outgoing"
            CallLog.Calls.MISSED_TYPE -> "Missed"
            else -> "Unknown"
        }
    }
//    private fun showDatePicker() {
//        val datePickerDialog = DatePickerDialog(
//            this,
//            { _, year, month, dayOfMonth ->
//                val calendar = Calendar.getInstance()
//                calendar.set(year, month, dayOfMonth)
//                val startDate = calendar.time
//
//                // Set the end date to the next day
//                calendar.add(Calendar.DAY_OF_MONTH, 1)
//                val endDate = calendar.time
//
//                fetchCallLogs(startDate, endDate)
//            },
//            initialYear,
//            initialMonth,
//            initialDayOfMonth
//        )
//        datePickerDialog.show()
//    }

    private fun showDatePicker() {
        val datePickerBuilder = MaterialDatePicker.Builder.dateRangePicker()
        val datePicker = datePickerBuilder.build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            val startDate = dateFormat.format(Date(selection.first ?: 0))
            val endDate = dateFormat.format(Date(selection.second ?: 0))

            binding.tvSelectedDateRange.text = "Selected Date Range: $startDate - $endDate"
            val format = SimpleDateFormat("MMM dd, yyyy", Locale.US)
            val date=  format.parse(startDate)
            val endDatee=format.parse(endDate)
          fetchCallLogs(date, endDatee)

            binding.rvCalls.adapter = adapter
        }

        datePicker.show(supportFragmentManager, "datePicker")
    }
}