package com.example.testriq.fakescreen_module

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.testriq.databinding.ActivityAutoClickCameraBinding
import com.example.testriq.databinding.ActivityFakeScreenBinding

class FakeScreenActivity : AppCompatActivity() {
    lateinit var binding: ActivityFakeScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFakeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        displayInstalledApps()

    }

    private fun displayInstalledApps() {
        val packageManager = packageManager
        val installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

        val appNames = ArrayList<String>()

        for (appInfo in installedApps) {
            appNames.add(appInfo.loadLabel(packageManager).toString())
        }

        // Now you can use appNames to populate your list view or perform any other operations
        // For example, you can log the names of the installed apps:
        for (appName in appNames) {
            Log.d("InstalledApp", appName)
        }
    }
}