package com.example.testriq.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testriq.databinding.ItemAppBinding
import com.example.testriq.databinding.ItemCallBinding
import com.example.testriq.databinding.ItemSmsBinding
import com.example.testriq.day_analyzer_module.model.Call

class CallAdapter : RecyclerView.Adapter<CallAdapter.CallViewHolder>() {
    private val callList = mutableListOf<Call>()

    fun setData(calls: List<Call>) {
        callList.clear()
        callList.addAll(calls)
        notifyDataSetChanged()
    }
    private var binding: ItemCallBinding? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallViewHolder {

        binding = ItemCallBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CallViewHolder(binding!!)
    }

    override fun onBindViewHolder(holder: CallViewHolder, position: Int) {
        val call = callList[position]
        holder.bind(call)
    }

    override fun getItemCount(): Int {
        return callList.size
    }

    private fun formatCallDuration(duration: Long): String {
        val hours = duration / 3600
        val minutes = (duration % 3600) / 60
        val seconds = duration % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

 inner  class CallViewHolder(val binding: ItemCallBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(call: Call) {
        binding.tvCallNumber.text ="Caller :${call.number}"

            binding.tvCallDate.text =  "Date :${call.date}"
            binding.tvDurration.text =  "Duration :${formatCallDuration(call.duration)}"
//            binding.tvCallType.text=     "Call Type :${call.callType}"
//            "Duration :${call.duration} seconds"
        }
    }
}
