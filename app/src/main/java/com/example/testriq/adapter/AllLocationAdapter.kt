package com.example.testriq.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.testriq.databinding.ActivityLocationBinding
import com.example.testriq.databinding.ItemAllLocationsBinding
import com.example.testriq.databinding.ItemAppBinding
import com.example.testriq.day_analyzer_module.LocationActivity
import com.example.testriq.day_analyzer_module.MapActivity
import com.example.testriq.day_analyzer_module.dao.TraceLocationDao
import com.example.testriq.day_analyzer_module.db.LocationDatabase
import com.example.testriq.day_analyzer_module.model.Call
import com.example.testriq.day_analyzer_module.model.TraceLocation
import com.example.testriq.model.App
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class AllLocationAdapter (private val context: Context, private val locationList: MutableList<TraceLocation>) :
    RecyclerView.Adapter<AllLocationAdapter.AllLocationViewHolder>() {
    private val allLocation =  mutableListOf<TraceLocation>()
    private lateinit var locationDatabase: LocationDatabase
    private lateinit var locationDao: TraceLocationDao
    var selectedItems: BooleanArray = BooleanArray(locationList.size) { false }

//    fun selectAll() {
//        for (item in locationList) {
//            item.isSelected = true
//        }
//        notifyDataSetChanged()
//    }

    fun selectAllItems() {
        selectedItems = BooleanArray(locationList.size) { true }
        notifyDataSetChanged()
    }

    fun unselectAllItems() {
        selectedItems = BooleanArray(locationList.size) { false }
        notifyDataSetChanged()
    }

    fun deleteSelectedTasks() {
//        val selectedApps = installedApps.filter { it.isSelected }
        val selectedTasks = locationList.filter {it.isSelected}
        locationList.removeAll(selectedTasks)
        notifyDataSetChanged()
        deleteTasksFromDatabase(selectedTasks)
    }

    private fun deleteTasksFromDatabase(tasks: List<TraceLocation>) {
        locationDatabase = Room.databaseBuilder(
            context,
            LocationDatabase::class.java,
            "map_data"
        ).build()
        locationDao = locationDatabase.getTasksDao()
        GlobalScope.launch {
            tasks.forEach {

                locationDao.delete(it)
            }
        }
    }
//    fun deleteItem(position: Int) {
//        val deletedItem = locationList[position]
//        locationList.removeAt(position)
//        notifyItemRemoved(position)
//
//        listener.onItemDelete(position, deletedItem)
//
//        // Store reference to deleted item in case of undo
//        val undoAction = { listener.onItemUndo(position, deletedItem) }
//
//        // Implement your own undo logic, e.g., showing a Snackbar with an undo option
//        Snackbar.make( binding!!.root,"Item deleted", Snackbar.LENGTH_LONG)
//            .setAction("Undo") { undoAction.invoke() }
//            .show()
//    }


    private var binding: ItemAllLocationsBinding? = null
    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): AllLocationViewHolder {
        binding = ItemAllLocationsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllLocationViewHolder(binding!!)
    }

    fun undoDelete(position: Int, item: TraceLocation) {
        locationList.add(position, item)
        notifyItemInserted(position)
    }
    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: AllLocationViewHolder, position: Int) {
        val locatList= locationList[position]
        holder.bind(locatList)

        holder.itemView.setOnClickListener {
            // Get the latitude and longitude values from the clicked item
            val latitude = locatList.latitude
            val longitude = locatList.longitude
            val timeMap=locatList.timestamp
            // Create an intent to start the MapActivity
            val intent = Intent(context, MapActivity::class.java)

            // Pass the latitude and longitude values as extras to the intent
            intent.putExtra(LocationActivity.EXTRA_LATITUDE, latitude)
            intent.putExtra(LocationActivity.EXTRA_LONGITUDE, longitude)
            intent.putExtra(LocationActivity.TIME_MAP, timeMap)
            // Start the MapActivity
            context.startActivity(intent)
        }

        holder.binding.cbItem.setOnCheckedChangeListener { buttonView, isChecked ->
            locatList.isSelected=isChecked
        }
    }

    override fun getItemCount(): Int {
        return locationList.size
    }

    inner class AllLocationViewHolder( val binding: ItemAllLocationsBinding) :
        RecyclerView.ViewHolder(binding.root){

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(locations: TraceLocation) {
            binding.apply {

                val timestamp = locations.timestamp

                val dateTime = Instant.ofEpochMilli(timestamp)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()

                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                val formattedDateTime = dateTime.format(formatter)

               tvlatitude.text =   "Latitude :${locations.latitude}"
                tvlongitude.text = "Longitude :${locations.longitude}"
//                tvTime.text = "Time :${locations.timestamp}"
                tvTime.text = "Time :${formattedDateTime}"


               cbItem.isChecked = locations.isSelected

                cbItem.setOnCheckedChangeListener { _, isChecked ->
                    locations.isSelected = isChecked
//                    updateLocations(locations)




            }
        }



    }}}