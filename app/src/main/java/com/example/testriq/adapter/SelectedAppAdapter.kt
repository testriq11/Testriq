package com.example.testriq.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.testriq.databinding.SelectedItemLayoutBinding
import com.example.testriq.model.App

class SelectedAppAdapter (private val context:Context,private val data: List<App>,private val listener: OnItemClickListener) : RecyclerView.Adapter<SelectedAppAdapter.ViewHolder>(){

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }


  inner class ViewHolder(val binding: SelectedItemLayoutBinding) : RecyclerView.ViewHolder(binding.root){

//        fun bind(appInfo: App) {
//            iconImageView.setImageDrawable(appInfo.icon)
//            iconImageView.setOnClickListener {
//                val packageName = appInfo.packageName
//                openApp(packageName)
//            }
//        }

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SelectedItemLayoutBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.binding.tvAppname.text = item.name
        holder.binding.ivSelected.setImageBitmap(item.icon)
     holder.binding.ivSelected.setOnClickListener {
            val packageName =item.packageName
            openApp(packageName)

        }
    }

    private fun openApp(packageName: String) {
        val packageManager = context.packageManager
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        intent?.let {
            context.startActivity(intent)
        } ?: run {
            // App not found
            Toast.makeText(context, "App not found", Toast.LENGTH_SHORT).show()
        }

    }

}


