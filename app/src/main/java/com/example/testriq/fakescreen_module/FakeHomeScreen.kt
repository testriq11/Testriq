package com.example.testriq.fakescreen_module

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.testriq.R
import com.example.testriq.adapter.AppAdapter
import com.example.testriq.adapter.SelectedAppAdapter
import com.example.testriq.databinding.ActivityFakeHomeScreenBinding
import com.example.testriq.model.App

class FakeHomeScreen : AppCompatActivity(),SelectedAppAdapter.OnItemClickListener {
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

        val adapter = SelectedAppAdapter(context,selectedApps!!,this)

        binding.selectedItemsRecyclerView.layoutManager = GridLayoutManager(this, 3)



        binding.selectedItemsRecyclerView.adapter = adapter

//        val selectedItems = intent.getParcelableArrayListExtra<App>("SELECTED_ITEMS")
//        selectedItems?.let {
//            appAdapter.setItems(it)
//            binding.selectedItemsRecyclerView.adapter = appAdapter
//        }

    
    }

    override fun onItemClick(position: Int) {

    }

}