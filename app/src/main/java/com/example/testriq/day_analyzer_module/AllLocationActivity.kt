package com.example.testriq.day_analyzer_module

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.testriq.adapter.AllLocationAdapter
import com.example.testriq.adapter.AppAdapter
import com.example.testriq.adapter.CallAdapter
import com.example.testriq.databinding.ActivityAllLocationBinding
import com.example.testriq.databinding.ActivityLocationBinding
import com.example.testriq.day_analyzer_module.dao.TraceLocationDao
import com.example.testriq.day_analyzer_module.db.LocationDatabase
import com.example.testriq.day_analyzer_module.model.TraceLocation
import com.example.testriq.model.App
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AllLocationActivity : AppCompatActivity(){

    lateinit var binding: ActivityAllLocationBinding
    private lateinit var adapter: AllLocationAdapter
    private lateinit var recyclerView: RecyclerView
    val context=this
    private lateinit var locationList: List<TraceLocation>
    private lateinit var locationDatabase: LocationDatabase
    private lateinit var locationDao: TraceLocationDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.swipeRefreshLayout.setOnRefreshListener {
            val locationList = intent.getSerializableExtra("locationList") as? ArrayList<TraceLocation>
            Log.e("loc",locationList.toString())

            adapter = AllLocationAdapter(context,locationList!!)
            binding.rvAllLocations.layoutManager = LinearLayoutManager(this)
            binding.rvAllLocations.adapter = adapter

            binding.swipeRefreshLayout.isRefreshing = false
        }
        binding.btnSelectAll.setOnClickListener {
            adapter.selectAll()
        }

        binding.btnDelete.setOnClickListener {
            adapter.deleteSelectedTasks()
        }
    }








}