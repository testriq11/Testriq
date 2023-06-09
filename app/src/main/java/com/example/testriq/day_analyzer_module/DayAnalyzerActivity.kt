package com.example.testriq.day_analyzer_module

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testriq.adapter.DayAnalyzerPagerAdapter
import com.example.testriq.adapter.SMSAdapter
import com.example.testriq.databinding.ActivityDayAnalyzerBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

//class DayAnalyzerActivity : AppCompatActivity(),DatePickerDialog.OnDateSetListener

class DayAnalyzerActivity : AppCompatActivity() {
    lateinit var binding:ActivityDayAnalyzerBinding
    val context=this
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDayAnalyzerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.llMessages.setOnClickListener {

            val messageintent= Intent(context,MessageActivity::class.java)
            startActivity(messageintent)
        }

        binding.llCalls.setOnClickListener {

            val callintent= Intent(context,CallActivity::class.java)
            startActivity(callintent)
        }
        binding.llLocations.setOnClickListener {

            val locationintent= Intent(context,LocationActivity::class.java)
            startActivity(locationintent)
        }

//
//        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
//        val tabLayout = binding.tabLayout
//        val viewPager = binding.viewPager

        // Setup the ViewPager with an adapter
//        viewPager.adapter = DayAnalyzerPagerAdapter(supportFragmentManager)

        // Connect the TabLayout with the ViewPager
//        tabLayout.setupWithViewPager(viewPager)
//
////        // pass it to rvLists layoutManager
//        binding.rvDayAnalyzer.setLayoutManager(layoutManager)
//        val smsMessages = fetchSMSMessages()
//        val adapter = SMSAdapter(smsMessages)
//        binding.rvDayAnalyzer.adapter = adapter

//        sharedPreferences = getSharedPreferences("AlarmPrefs", Context.MODE_PRIVATE)
//
//
//        binding.btnDatePicker.setOnClickListener {
//            showDatePicker()
//        }
//        fetchSmsData()

    }

//    private fun showDatePicker() {
//        val datePickerBuilder = MaterialDatePicker.Builder.dateRangePicker()
//        val datePicker = datePickerBuilder.build()
//
//        datePicker.addOnPositiveButtonClickListener { selection ->
//            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
//            val startDate = dateFormat.format(Date(selection.first ?: 0))
//            val endDate = dateFormat.format(Date(selection.second ?: 0))
//
//            binding.tvSelectedDateRange.text = "Selected Date Range: $startDate - $endDate"
//            val format = SimpleDateFormat("MMM dd, yyyy", Locale.US)
//            val date=  format.parse(startDate)
//            val endDatee=format.parse(endDate)
//            val messages = fetchSMSMessages(date, endDatee)
//            val adapter = SMSAdapter(messages)
//            binding.rvDayAnalyzer.adapter = adapter
//        }
//
//        datePicker.show(supportFragmentManager, "datePicker")
//    }
//
//    fun fetchSMSMessages(startDate: Date, endDate: Date): List<SMSMessage> {
//        val messages = mutableListOf<SMSMessage>()
//        val selection = "date BETWEEN ${startDate.time} AND ${endDate.time}"
//        val sortOrder = "date ASC"
//
//        val cursor = contentResolver.query(
//            Uri.parse("content://sms/inbox"),
//            null,
//            selection,
//            null,
//            sortOrder
//        )
//
//        cursor?.use {
//            val indexBody = cursor.getColumnIndexOrThrow("body")
//            val indexAddress = cursor.getColumnIndexOrThrow("address")
//            val indexDate = cursor.getColumnIndexOrThrow("date")
//
//            while (cursor.moveToNext()) {
//                val body = cursor.getString(indexBody)
//                val address = cursor.getString(indexAddress)
//                val dateInMillis = cursor.getLong(indexDate)
//
//                val date = Date(dateInMillis)
//                val message = SMSMessage(body, address, date)
//                messages.add(message)
//            }
//        }
//
//        return messages
//    }
//
//    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
//        TODO("Not yet implemented")
//    }
/////////////////////////////////////////////////////////

//    private fun fetchSmsData(): List<SMSMessage> {
//        val smsList = mutableListOf<SMSMessage>()
//
//        val projection = arrayOf(
//            "_id",
//            "address",
//            "body",
//            "date"
//        )
//
//        val selection = "date > ?"
//        val selectionArgs = arrayOf("1671590400000")
//
//        val cursor = contentResolver.query(
//            Telephony.Sms.CONTENT_URI,
//            projection,
//            selection,
//            selectionArgs,
//            "date ASC"
//        )
//
//        cursor?.use {
//            val idColumn = it.getColumnIndex("_id")
//            val senderColumn = it.getColumnIndex("address")
//            val messageColumn = it.getColumnIndex("body")
//            val timestampColumn = it.getColumnIndex("date")
//
//            while (it.moveToNext()) {
//                val id = it.getString(idColumn)
//                val sender = it.getString(senderColumn)
//                val message = it.getString(messageColumn)
//                val timestamp = it.getLong(timestampColumn)
//
//                val sms = SMSMessage(id, sender, message, timestamp)
//                smsList.add(sms)
//            }
//        }
//
//        return smsList
//    }


}