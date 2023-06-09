package com.example.testriq.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testriq.databinding.ItemCallBinding
import com.example.testriq.databinding.ItemLocationsBinding
import com.example.testriq.day_analyzer_module.model.TraceLocation

class TraceLocationAdapter(private val traceLocations: List<TraceLocation>) : RecyclerView.Adapter<TraceLocationAdapter.LocationViewHolder>() {

    private var binding: ItemLocationsBinding? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder{

        binding = ItemLocationsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocationViewHolder(binding!!)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val traceLocation = traceLocations[position]
        holder.bind(traceLocation)
    }

    override fun getItemCount(): Int {
        return traceLocations.size
    }

    class LocationViewHolder(val binding: ItemLocationsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(traceLocation: TraceLocation) {
            // Bind traceLocation data to your UI elements
//            binding.tvCity.text ="Caller :${traceLocation.id}"
            binding.tvlatitude.text ="Caller :${traceLocation.latitude}"
            binding.tvlongitude.text ="Caller :${traceLocation.longitude}"
        }
    }
}
