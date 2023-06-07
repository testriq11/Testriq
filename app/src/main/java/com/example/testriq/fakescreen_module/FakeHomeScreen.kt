package com.example.testriq.fakescreen_module

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.testriq.adapter.SelectedAppAdapter
import com.example.testriq.databinding.ActivityFakeHomeScreenBinding
import com.example.testriq.model.App

class FakeHomeScreen : AppCompatActivity(),SelectedAppAdapter.OnItemClickListener {
 val context =this
    private lateinit var binding: ActivityFakeHomeScreenBinding
    private var initialBottomInset: Int = 0

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFakeHomeScreenBinding.inflate(layoutInflater)
        val view = binding.root

        // Set the window flags to lock the bottom home screen
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        setContentView(view)
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE
        }
//        // Get the initial bottom inset value
//        val windowInsetsController = ViewCompat.getWindowInsetsController(window.decorView)
//        initialBottomInset = windowInsetsController?.insets?.bottom ?: 0



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