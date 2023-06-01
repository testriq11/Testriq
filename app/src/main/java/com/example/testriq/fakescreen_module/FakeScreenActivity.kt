package com.example.testriq.fakescreen_module

import android.app.Activity
import android.app.KeyguardManager
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testriq.adapter.AppAdapter
import com.example.testriq.databinding.ActivityFakeScreenBinding
import com.example.testriq.model.App
import com.google.android.material.snackbar.Snackbar


class FakeScreenActivity : AppCompatActivity() {
    val context=this
    private val DEVICE_ADMIN_REQUEST_CODE = 1
    lateinit var binding: ActivityFakeScreenBinding
    private lateinit var appRecyclerView: RecyclerView
    private lateinit var appAdapter: AppAdapter
    private lateinit var appList: List<App>
    private lateinit var deletedApps: MutableList<App>
    private lateinit var keyguardManager: KeyguardManager
    private lateinit var devicePolicyManager: DevicePolicyManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFakeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.appRecyclerView.layoutManager=LinearLayoutManager(this)
//        appList=displayInstalledApps()
//        displayInstalledApps()
//        appAdapter = AppAdapter(appList)
//        binding.appRecyclerView.adapter = appAdapter
//        deletedApps = mutableListOf()
          appList=displayInstalledApps()
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
//
//        // pass it to rvLists layoutManager
        binding.appRecyclerView.setLayoutManager(layoutManager)

        // initialize the adapter,
        // and pass the required argument
        appAdapter = AppAdapter(context , appList as MutableList<App>)

        // attach adapter to the recycler view
        binding.appRecyclerView.adapter = appAdapter

        requestDeviceAdmin()

        binding.btnDelete.setOnClickListener {
//            appAdapter.deleteSelectedItems()
        }
    }

    private fun requestDeviceAdmin() {
        val componentName = ComponentName(this, MyDeviceAdminReceiver::class.java)
        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName)
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Please grant device admin permission")
        startActivityForResult(intent, DEVICE_ADMIN_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == DEVICE_ADMIN_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // Device admin permission granted
                freezeHomeScreen()
            } else {
                // Device admin permission denied
                // Handle accordingly
            }
        }
    }

    private fun freezeHomeScreen() {
        // Disable keyguard
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)

        // Set lock task mode if the device is running Android 5.0 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            devicePolicyManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager

            if (!keyguardManager.isKeyguardLocked) {
                startLockTask()
            } else {
                // Request device admin permission to set lock task mode
//                val intent = devicePolicyManager.createConfirmDeviceCredentialIntent(
//                    null,
//                    null
//                )

                startActivityForResult(intent, 6)
            }
        }
//        val devicePolicyManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
//        val componentName = ComponentName(this, MyDeviceAdminReceiver::class.java)
//        val dpm = getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager
//        val isProfileOwner = dpm.isProfileOwnerApp("com.example.testriq")
//        val isDeviceOwner = dpm.isDeviceOwnerApp("com.example.testriq")
//
////        val adminComponent = ComponentName(context, MyDeviceAdminReceiver::class.java)
//        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
//        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName)
//        startActivityForResult(intent, 45)
//        intent.putExtra(
//            DevicePolicyManager.EXTRA_ADD_EXPLANATION,
//            "Please enable device admin access."
//        )

//         Disable the keyguard and set the lock task mode
//        devicePolicyManager.setKeyguardDisabled(componentName, true)
      //  devicePolicyManager.setLockTaskPackages(componentName, arrayOf(packageName))

        // Start the lock task mode
        startLockTask()
    }

    private fun displayInstalledApps(): List<App> {



        val packageManager = applicationContext.packageManager
        val installedApps = mutableListOf<App>()

        val packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        for (appInfo in packages) {
            val appName = appInfo.loadLabel(packageManager).toString()
            val packageName = appInfo.packageName
            val appIcon = appInfo.loadIcon(packageManager)

            val app = App(appName, packageName, appIcon)
            installedApps.add(app)
            Log.d("InstalledApp", appName)
        }
////        val packageManager = packageManager
////        val installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
//
////        appList = listOf(App())
////
////        for (appInfo in installedApps) {
////            appNames.add(appInfo.loadLabel(packageManager).toString())
////        }
////
////        // Now you can use appNames to populate your list view or perform any other operations
////        // For example, you can log the names of the installed apps:
////        for (appName in appNames) {
////            Log.d("InstalledApp", appName)
////        }
return installedApps
    }

//    private fun deleteSelectedApps() {
//        deletedApps.clear()
//        val iterator = appList.iterator()
//        while (iterator.hasNext()) {
//            val app = iterator.next()
//            if (app.) {
//                deletedApps.add(app)
//                iterator.remove()
//            }
//        }
//        appAdapter.notifyDataSetChanged()
//        showUndoSnackbar()
//    }



//    private fun showUndoSnackbar() {
//        val snackbar = Snackbar.make(
//            binding.clFakeScreen,
//           "Apps deleted",
//            Snackbar.LENGTH_LONG
//        )
//        snackbar.setAction("Undo") {
//            undoDeletion()
//        }
//        snackbar.show()
//    }
//
//    private fun undoDeletion() {
//        appList.addAll(deletedApps)
//        appAdapter.notifyDataSetChanged()
//        deletedApps.clear()
//    }

}
