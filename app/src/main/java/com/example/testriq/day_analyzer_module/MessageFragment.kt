package com.example.testriq.day_analyzer_module

import android.app.DatePickerDialog
import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testriq.R
import com.example.testriq.adapter.SMSAdapter
import com.example.testriq.databinding.ActivityDayAnalyzerBinding
import com.example.testriq.databinding.FragmentMessageBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*


class MessageFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    lateinit var binding: FragmentMessageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMessageBinding.inflate(inflater,container,false);
        val view = binding.root;
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        binding.rvDayAnalyzer.setLayoutManager(layoutManager)


        binding.btnDatePicker.setOnClickListener {
            showDatePicker()
        }

        return view;
    }

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
            val messages = fetchSMSMessages(date, endDatee)
            val adapter = SMSAdapter(messages)
            binding.rvDayAnalyzer.adapter = adapter
        }

        datePicker.show(requireFragmentManager(), "datePicker")
    }

    fun fetchSMSMessages(startDate: Date, endDate: Date): List<SMSMessage> {
        val messages = mutableListOf<SMSMessage>()
        val selection = "date BETWEEN ${startDate.time} AND ${endDate.time}"
        val sortOrder = "date ASC"
        val contentResolver: ContentResolver = requireContext().contentResolver

        val cursor = contentResolver.query(
            Uri.parse("content://sms/inbox"),
            null,
            selection,
            null,
            sortOrder
        )

        cursor?.use {
            val indexBody = cursor.getColumnIndexOrThrow("body")
            val indexAddress = cursor.getColumnIndexOrThrow("address")
            val indexDate = cursor.getColumnIndexOrThrow("date")

            while (cursor.moveToNext()) {
                val body = cursor.getString(indexBody)
                val address = cursor.getString(indexAddress)
                val dateInMillis = cursor.getLong(indexDate)

                val date = Date(dateInMillis)
                val message = SMSMessage(body, address, date)
                messages.add(message)
            }
        }

        return messages
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        TODO("Not yet implemented")
    }



        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}