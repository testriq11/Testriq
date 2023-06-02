package com.example.testriq.fakescreen_module

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.testriq.R
import com.example.testriq.adapter.AppAdapter
import com.example.testriq.databinding.ActivityFakeHomeScreenBinding
import com.example.testriq.model.App

class FakeHomeScreen : AppCompatActivity() {
 val context =this
    private lateinit var binding: ActivityFakeHomeScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFakeHomeScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

//        val selectedItems = intent.getStringExtra("selectedItems")
        val selectedApps = intent.getSerializableExtra("selectedApps") as? ArrayList<App>
        Log.e("items",selectedApps .toString())

//        val selectedItems = intent.getParcelableArrayListExtra<App>("SELECTED_ITEMS")
//        selectedItems?.let {
//            appAdapter.setItems(it)
//            binding.selectedItemsRecyclerView.adapter = appAdapter
//        }
    }
}