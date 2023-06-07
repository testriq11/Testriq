package com.example.testriq.adapter

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testriq.databinding.ItemSmsBinding
import com.example.testriq.databinding.SelectedItemLayoutBinding
import com.example.testriq.day_analyzer_module.SMSMessage
import java.text.SimpleDateFormat
import java.util.*

class SMSAdapter(private val messages: List<SMSMessage>) :
    RecyclerView.Adapter<SMSAdapter.SMSViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SMSViewHolder {
                val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSmsBinding.inflate(inflater, parent, false)
        return SMSViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SMSViewHolder, position: Int) {
        val message = messages[position]
        holder.bind(message)
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    class SMSViewHolder(val binding: ItemSmsBinding) : RecyclerView.ViewHolder(binding.root) {


        fun bind(message: SMSMessage) {
          binding.messageTextView.text = "Message :${message.body}"
            binding.messageTextView.setTypeface(null, Typeface.BOLD)
            binding.senderTextView.text = "Sender : ${message.address}"
            binding.senderTextView.setTypeface(null, Typeface.BOLD)
           binding.timestampTextView.text ="Date :${message.date.toString()}"
            binding.timestampTextView.setTypeface(null, Typeface.BOLD)
        }
    }
}

//class SMSAdapter(private val messages: List<SMSMessage>) :
//    RecyclerView.Adapter<SMSAdapter.SMSViewHolder>() {
//
//    inner class SMSViewHolder(val binding: ItemSmsBinding) : RecyclerView.ViewHolder(binding.root) {
//
//
//
//        fun bind(message: SMSMessage) {
//            binding.senderTextView.text = message.sender
//            binding.messageTextView.text = message.message
//            val timestamp = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
//                .format(Date(message.timestamp))
//            binding.timestampTextView.text = timestamp
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SMSViewHolder {
//        val inflater = LayoutInflater.from(parent.context)
//        val binding = ItemSmsBinding.inflate(inflater, parent, false)
//        return SMSViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: SMSViewHolder, position: Int) {
//        val message = messages[position]
//        holder.bind(message)
//    }
//
//    override fun getItemCount(): Int {
//        return messages.size
//    }
//}
