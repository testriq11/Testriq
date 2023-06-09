package com.example.testriq.camera_module

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.*
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.*
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.core.impl.ImageCaptureConfig
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.example.testriq.Broadcast.AudioBoradCastEnd
import com.example.testriq.Broadcast.CameraAlarmReceiver
import com.example.testriq.databinding.ActivityAutoClickCameraBinding
import com.google.android.material.snackbar.Snackbar
import com.google.common.util.concurrent.ListenableFuture
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AutoClickCamera : AppCompatActivity() {

    private lateinit var runnable: Runnable
    private lateinit var binding: ActivityAutoClickCameraBinding
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraSelector: CameraSelector
    private var imageCapture: ImageCapture? = null
    private lateinit var imgCaptureExecutor: ExecutorService
    private lateinit var filePath: String
    lateinit var uri: Uri
    private var ALARAM_REQ_CODE :Int = 202
    lateinit var file: File
    lateinit var image: Bitmap
    private var counter = 0
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var timer: Timer


//    val clickRunnable = object : Runnable {
//        private var count = 0

        //        override fun run() {
//            if (count < binding.qtyImg.text.toString().toInt()-1) {
//                binding.imgCaptureBtn.performClick()
//                count++
//                handler.postDelayed(this, delayInMillis)
//            }
//        }
//    }
//        override fun run() {
//            if (count < binding.qtyImg.text.toString().toInt()-1) {
//                binding.imgCaptureBtn.performClick()
//                count++
//                handler.postDelayed(this, delayInMillis)
//
//            }
//        }

//    }
    // Set the delay between each click (in milliseconds)
    private val delayInMillis = 8000L
    private val handler = Handler()
    val context=this
    @RequiresApi(Build.VERSION_CODES.O)
    private val cameraPermissionResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted ->
            if (permissionGranted) {
                startCamera()
            } else {
                Snackbar.make(
                    binding.root,
                    "The camera permission is necessary",
                    Snackbar.LENGTH_INDEFINITE
                ).show()
            }
        }



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityAutoClickCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val endt = intent?.getStringExtra("datetimeEnd")

        cameraExecutor = Executors.newSingleThreadExecutor()

        val gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(event: MotionEvent): Boolean {
                // Implement your auto-click logic here
                // This method is triggered when a single tap occurs on the preview view
                startCamera()
                return true
            }
        })

        binding.preview.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }
        // Initialize CameraX
//        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
//        cameraProviderFuture.addListener({
//            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
//
//            // Use the cameraProvider to set up your camera preview and capture
//            // logic here, as per your requirements
//            // ...
//
//            // Schedule the image capture after a delay of 5 seconds
//            val preview = Preview.Builder().build().also {
//                it.setSurfaceProvider(binding.preview.surfaceProvider)
//            }
//            val useCaseConfig = ImageCaptureConfig.Builder()
//                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
//                .build()
//
//            // Bind the image capture use case to the lifecycle
//            val imageCaptureUseCase = ImageCapture(useCaseConfig)
//            cameraProvider.unbindAll()
//            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
//            Handler(Looper.getMainLooper()).postDelayed({
//               takePhoto()
//            }, 5000)
//        }, ContextCompat.getMainExecutor(this))
        startCamera()

        Log.e("myValue", endt.toString())


        val format = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())

        val formatter = DateTimeFormatter.ofPattern("EEE MMM d HH:mm:ss 'GMT'XXX yyyy", Locale.ENGLISH)
        val dateTime = LocalDateTime.parse(endt, formatter)
        val zone = ZoneId.of("GMT+05:30")
        val zonedDateTime = dateTime.atZone(zone)

        val triggerTime: Long = zonedDateTime.toEpochSecond() * 1000
        try {
//            var date: Date = format.parse(endt)
//            var time=Integer.parseInt(date.toString())

//            var triggerTime:Long=System.currentTimeMillis()+(zonedDateTime)
            val iBroadcast= Intent(this, AudioBoradCastEnd::class.java)
            val pi= PendingIntent.getBroadcast(this,ALARAM_REQ_CODE,iBroadcast,
                PendingIntent.FLAG_IMMUTABLE)
            alarmManager.set(AlarmManager.RTC_WAKEUP,triggerTime,pi)
            // Further processing with the date object...
        } catch (e: ParseException) {
            e.printStackTrace()
        }

//        val filter = IntentFilter(CameraAlarmReceiver.ACTION_CLOSE_ACTIVITY)
//        registerReceiver(receiver, filter)
//        val iBroadcast= Intent(this, AudioBoradCastEnd::class.java)
//            intent.putExtra("editedText", binding.edtQtyImages.text.toString().toInt())
        val handler = Handler()
        runnable = Runnable {
            // Finish the activity
            finish()
        }

        // Schedule the activity closure
        handler.postDelayed(runnable, triggerTime - System.currentTimeMillis())



        var rqty = intent.getStringExtra("Qty")

        Log.e("rqt", rqty.toString())
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        imgCaptureExecutor = Executors.newSingleThreadExecutor()

        cameraPermissionResult.launch(android.Manifest.permission.CAMERA)

//        binding.imgCaptureBtn.setOnClickListener {
//            var i=0
//          var qty=binding.qtyImg.text.toString().toIntOrNull()
//
//
////            while (i <= qty!!) {
////                val delayMillis = 8000L // 2 seconds delay
//                val delayMillis = 5000L
//
//                val handler = Handler(Looper.getMainLooper())
//                val runnable = Runnable {
//                    // Your delayed task code here
//                    if (qty != null) {
//                        while (i < qty){
//                            binding.imgCaptureBtn.performClick()
//                            takePhoto()
//                            i++
//                        }
//                    }
//
//                }
//                handler.postDelayed(runnable, delayMillis)
//
////                Handler(Looper.getMainLooper()).postDelayed({
////                    binding.imgCaptureBtn.performClick()
////                    takePhoto()
////                }, delayMillis)
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    animateFlash()
//                }
////                i++
//               // handler.removeCallbacks(runnable)
////            }
//
//        }


//        binding.imgCaptureBtn.setOnClickListener {
//            val qty = binding.qtyImg.text.toString()
//
////            binding.imgCaptureBtn.performClick()
//            for (i in 0 until qty.toInt()) {
////
//                takePhoto()
//            }
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                animateFlash()
//            }
//        }
//        val qty = binding.qtyImg.text.toString()
//
//        binding.imgCaptureBtn.setOnClickListener {
//            startAutoClick()
//            Toast.makeText(context,"Images Saved ", Toast.LENGTH_LONG).show()
//
//        }

//        binding.switchBtn.setOnClickListener {
//            cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
//                CameraSelector.DEFAULT_FRONT_CAMERA
//            } else {
//                CameraSelector.DEFAULT_BACK_CAMERA
//            }
//            startCamera()
//        }
//        binding.galleryBtn.setOnClickListener {
//            val intent = Intent(this, GalleryActivity::class.java)
//            startActivity(intent)
//        }

    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        startCamera()
    }

    override fun onPause() {
        super.onPause()
        cameraExecutor.shutdown()
    }
    override fun onDestroy() {
        super.onDestroy()
        // Unregister the broadcast receiver when the activity is destroyed
//        unregisterReceiver(broadcastReceiver)
        cameraExecutor.shutdown()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startAutoClick() {

        takePhoto()

//        handler.post(clickRunnable)
    }

    // Call this function to stop auto-clicking the button
    private fun stopAutoClick() {
//        handler.removeCallbacks(clickRunnable)
    }
//    Make sure you replace button with your actual button object. The code sets the desired quantity of clicks, the delay between each click, and creates a Handler and a Runnable to perform the button click. The startAutoClick() function starts the auto-clicking process, and the stopAutoClick() function stops it by removing the callbacks.



    private fun deleteFolderFromInternalStorage() {
        val folder = File(
            Environment.getExternalStorageDirectory().absolutePath, ""
        )
        if (folder.exists()) {
            val deleted = folder.deleteRecursively()
            if (deleted) {
                Toast.makeText(this,"Folder Deleted Succesfully",Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this,"Failed to  Deleted Succesfully",Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this,"Folder Doesn't Exist",Toast.LENGTH_LONG).show()
        }
    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun startCamera() {
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(binding.preview.surfaceProvider)
        }
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)

                Handler(Looper.getMainLooper()).postDelayed({
                    takePhoto()

                }, 30000)
                Toast.makeText(context,"Image Saved in Folder",Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Log.d(TAG, "Use case binding failed")
            }
        }, ContextCompat.getMainExecutor(this))
    }






    @RequiresApi(Build.VERSION_CODES.O)
    private fun takePhoto() {



//            binding.imgCaptureBtn.performClick()
        imageCapture?.let {
//            val fileName = "JPEG_${System.currentTimeMillis()}"
//            val file = File(externalMediaDirs[0], fileName)
//            val file = File(
//                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
//                "i"
//            )
            val timestamp = LocalDateTime.now().toString().replace(":", "-")
            val fileName = "img$timestamp-${Random().nextInt(10000)}.jpg"
            val file = File(externalMediaDirs.first(), fileName)
//
            val outputFileOptions = ImageCapture.OutputFileOptions.Builder(file).build()
            it.takePicture(
                outputFileOptions,
                imgCaptureExecutor,
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        saveImageToInternalStorage(context ,file.toUri())
//var durartion=5000L
////                                    Toast.makeText(this@MainActivity," Image Saved in Internal Storage",Toast.LENGTH_LONG).show()
////                                  var Toast toast = Toast.makeText(context, "GAME OVER!\nScore: " + score, duration);
//                         var toast= Toast.makeText(context, "Image SAved ",durartion)
//                                    toast.show();
                        Log.i(TAG, "The image has been saved in ${file.toUri()}")

                    }

                    override fun onError(exception: ImageCaptureException) {
                        Toast.makeText(
                            binding.root.context,
                            "Error taking photo",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.d(TAG, "Error taking photo:$exception")


                    }




                })
        }

    }

//        imageCapture?.let {
////            val fileName = "JPEG_${System.currentTimeMillis()}"
////            val file = File(externalMediaDirs[0], fileName)
////            val file = File(
////                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
////                "i"
////            )
//            val timestamp = LocalDateTime.now().toString().replace(":", "-")
//            val fileName="img$timestamp-${Random().nextInt(10000)}.jpg"
//            val file = File(externalMediaDirs.first(), fileName)
////
//            val outputFileOptions = ImageCapture.OutputFileOptions.Builder(file).build()
//            it.takePicture(
//                outputFileOptions,
//                imgCaptureExecutor,
//                object : ImageCapture.OnImageSavedCallback {
//                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
////                        saveImageToInternalStorage(context ,file.toUri())
//                        Log.i(TAG, "The image has been saved in ${file.toUri()}")
//                    }
//
//                    override fun onError(exception: ImageCaptureException) {
//                        Toast.makeText(
//                            binding.root.context,
//                            "Error taking photo",
//                            Toast.LENGTH_LONG
//                        ).show()
//                        Log.d(TAG, "Error taking photo:$exception")
//                    }
//
//                })
//        }
//################################################################
//        val folder = File(Environment.getExternalStorageDirectory(), "MyAppFolder")
//        if (!folder.exists()) {
//            folder.mkdirs()
//        }
//        val folderPath = folder.getAbsolutePath()
//        filePath = File(folderPath, "/" + title + ".jpg").toString()
//
////        val photoFile = File(externalMediaDirs.first(), "image.jpg")
//        val path = filesDir.absolutePath
//        val outputDirectory =path
//        val filename = "example.jpg"
//        val file = File("$filesDir/$filename")

//        val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()
//
//        imageCapture?.takePicture(outputOptions, ContextCompat.getMainExecutor(this),
//            object : ImageCapture.OnImageSavedCallback {
//                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
//                    // Image capture success
////                    val uri=file.toUri().toString()
//                    saveImageToInternalStorage(context ,file.toUri())
//
//                    Log.i(TAG, "The image has been saved in ${file.toUri()}")
//                    // Access the saved image file using outputFileResults.savedUri
//                }
//
//                override fun onError(exception: ImageCaptureException) {
//                    // Handle image capture errors
//                }
//            })

    fun saveImageToInternalStorage(context: Context, imageUri: Uri) {
        val contentResolver: ContentResolver = context.contentResolver

        // Create a new file in internal storage
        val internalStorageDir: File = context.filesDir
        val imageFile = File(internalStorageDir, "my_image.jpg")

        var inputStream: InputStream? = null
        var outputStream: FileOutputStream? = null

        try {
            // Retrieve the image data from the URI
            inputStream = contentResolver.openInputStream(imageUri)

            // Copy the image data to the output file
            outputStream = FileOutputStream(imageFile)
            val buffer = ByteArray(4 * 1024) // 4KB buffer
            var bytesRead: Int
            while (inputStream!!.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                // Close the input stream and output stream
                inputStream?.close()
                outputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun animateFlash() {
        binding.root.postDelayed({
            binding.root.foreground = ColorDrawable(Color.WHITE)
            binding.root.postDelayed({
                binding.root.foreground = null
            }, 50)
        }, 100)
    }

    companion object {
        val TAG = "MainActivity"
    }
}


//class MainActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityMainBinding
//    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
//    private lateinit var cameraSelector: CameraSelector
//    private var imageCapture: ImageCapture? = null
//    private lateinit var imgCaptureExecutor: ExecutorService
//    private lateinit var filePath: String
//    lateinit var uri:Uri
//    lateinit var file: File
//    lateinit var image:Bitmap
//    private var counter = 0
//    val clickRunnable = object : Runnable {
//        private var count = 0
//
////        override fun run() {
////            if (count < binding.qtyImg.text.toString().toInt()-1) {
////                binding.imgCaptureBtn.performClick()
////                count++
////                handler.postDelayed(this, delayInMillis)
////            }
////        }
////    }
//    override fun run() {
//        if (count < binding.qtyImg.text.toString().toInt()-1) {
//            binding.imgCaptureBtn.performClick()
//            count++
//            handler.postDelayed(this, delayInMillis)
//        }
//    }
//}
//    // Set the delay between each click (in milliseconds)
//    private val delayInMillis = 8000L
//    private val handler = Handler()
//    val context=this
//    private val cameraPermissionResult =
//        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted ->
//            if (permissionGranted) {
//                startCamera()
//            } else {
//                Snackbar.make(
//                    binding.root,
//                    "The camera permission is necessary",
//                    Snackbar.LENGTH_INDEFINITE
//                ).show()
//            }
//        }
//
//
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        var rqty = intent.getStringExtra("Qty")
//
//        Log.e("rqt", rqty.toString())
//        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
//        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
//
//        imgCaptureExecutor = Executors.newSingleThreadExecutor()
//
//        cameraPermissionResult.launch(android.Manifest.permission.CAMERA)
//
////        binding.imgCaptureBtn.setOnClickListener {
////            var i=0
////          var qty=binding.qtyImg.text.toString().toIntOrNull()
////
////
//////            while (i <= qty!!) {
//////                val delayMillis = 8000L // 2 seconds delay
////                val delayMillis = 5000L
////
////                val handler = Handler(Looper.getMainLooper())
////                val runnable = Runnable {
////                    // Your delayed task code here
////                    if (qty != null) {
////                        while (i < qty){
////                            binding.imgCaptureBtn.performClick()
////                            takePhoto()
////                            i++
////                        }
////                    }
////
////                }
////                handler.postDelayed(runnable, delayMillis)
////
//////                Handler(Looper.getMainLooper()).postDelayed({
//////                    binding.imgCaptureBtn.performClick()
//////                    takePhoto()
//////                }, delayMillis)
////
////                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
////                    animateFlash()
////                }
//////                i++
////               // handler.removeCallbacks(runnable)
//////            }
////
////        }
//
//
////        binding.imgCaptureBtn.setOnClickListener {
////            val qty = binding.qtyImg.text.toString()
////
//////            binding.imgCaptureBtn.performClick()
////            for (i in 0 until qty.toInt()) {
//////
////                takePhoto()
////            }
////            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
////                animateFlash()
////            }
////        }
//        val qty = binding.qtyImg.text.toString()
//
//        binding.imgCaptureBtn.setOnClickListener {
//            startAutoClick()
//         Toast.makeText(context,"Images Saved ",Toast.LENGTH_LONG).show()
//
//        }
//
//        binding.switchBtn.setOnClickListener {
//            cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
//                CameraSelector.DEFAULT_FRONT_CAMERA
//            } else {
//                CameraSelector.DEFAULT_BACK_CAMERA
//            }
//            startCamera()
//        }
//        binding.galleryBtn.setOnClickListener {
//            val intent = Intent(this, GalleryActivity::class.java)
//            startActivity(intent)
//        }
//
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun startAutoClick() {
//
//        takePhoto()
//
//        handler.post(clickRunnable)
//    }
//
//    // Call this function to stop auto-clicking the button
//    private fun stopAutoClick() {
//        handler.removeCallbacks(clickRunnable)
//    }
////    Make sure you replace button with your actual button object. The code sets the desired quantity of clicks, the delay between each click, and creates a Handler and a Runnable to perform the button click. The startAutoClick() function starts the auto-clicking process, and the stopAutoClick() function stops it by removing the callbacks.
//
//
//
//
//
//
//
//    private fun startCamera() {
//        val preview = Preview.Builder().build().also {
//            it.setSurfaceProvider(binding.preview.surfaceProvider)
//        }
//        cameraProviderFuture.addListener({
//            val cameraProvider = cameraProviderFuture.get()
//
//            imageCapture = ImageCapture.Builder().build()
//
//            try {
//                cameraProvider.unbindAll()
//                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
//            } catch (e: Exception) {
//                Log.d(TAG, "Use case binding failed")
//            }
//        }, ContextCompat.getMainExecutor(this))
//    }
//
//
//
//
//
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun takePhoto() {
//
//
//
////            binding.imgCaptureBtn.performClick()
//                    imageCapture?.let {
////            val fileName = "JPEG_${System.currentTimeMillis()}"
////            val file = File(externalMediaDirs[0], fileName)
////            val file = File(
////                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
////                "i"
////            )
//                        val timestamp = LocalDateTime.now().toString().replace(":", "-")
//                        val fileName = "img$timestamp-${Random().nextInt(10000)}.jpg"
//                        val file = File(externalMediaDirs.first(), fileName)
////
//                        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(file).build()
//                        it.takePicture(
//                            outputFileOptions,
//                            imgCaptureExecutor,
//                            object : ImageCapture.OnImageSavedCallback {
//                                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
////                        saveImageToInternalStorage(context ,file.toUri())
////var durartion=5000L
//////                                    Toast.makeText(this@MainActivity," Image Saved in Internal Storage",Toast.LENGTH_LONG).show()
//////                                  var Toast toast = Toast.makeText(context, "GAME OVER!\nScore: " + score, duration);
////                         var toast= Toast.makeText(context, "Image SAved ",durartion)
////                                    toast.show();
//                                    Log.i(TAG, "The image has been saved in ${file.toUri()}")
//                                }
//
//                                override fun onError(exception: ImageCaptureException) {
//                                    Toast.makeText(
//                                        binding.root.context,
//                                        "Error taking photo",
//                                        Toast.LENGTH_LONG
//                                    ).show()
//                                    Log.d(TAG, "Error taking photo:$exception")
//
//
//                            }
//
//
//
//
//                            })
//                    }
//
//        }
//
////        imageCapture?.let {
//////            val fileName = "JPEG_${System.currentTimeMillis()}"
//////            val file = File(externalMediaDirs[0], fileName)
//////            val file = File(
//////                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
//////                "i"
//////            )
////            val timestamp = LocalDateTime.now().toString().replace(":", "-")
////            val fileName="img$timestamp-${Random().nextInt(10000)}.jpg"
////            val file = File(externalMediaDirs.first(), fileName)
//////
////            val outputFileOptions = ImageCapture.OutputFileOptions.Builder(file).build()
////            it.takePicture(
////                outputFileOptions,
////                imgCaptureExecutor,
////                object : ImageCapture.OnImageSavedCallback {
////                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
//////                        saveImageToInternalStorage(context ,file.toUri())
////                        Log.i(TAG, "The image has been saved in ${file.toUri()}")
////                    }
////
////                    override fun onError(exception: ImageCaptureException) {
////                        Toast.makeText(
////                            binding.root.context,
////                            "Error taking photo",
////                            Toast.LENGTH_LONG
////                        ).show()
////                        Log.d(TAG, "Error taking photo:$exception")
////                    }
////
////                })
////        }
////################################################################
////        val folder = File(Environment.getExternalStorageDirectory(), "MyAppFolder")
////        if (!folder.exists()) {
////            folder.mkdirs()
////        }
////        val folderPath = folder.getAbsolutePath()
////        filePath = File(folderPath, "/" + title + ".jpg").toString()
////
//////        val photoFile = File(externalMediaDirs.first(), "image.jpg")
////        val path = filesDir.absolutePath
////        val outputDirectory =path
////        val filename = "example.jpg"
////        val file = File("$filesDir/$filename")
//
////        val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()
////
////        imageCapture?.takePicture(outputOptions, ContextCompat.getMainExecutor(this),
////            object : ImageCapture.OnImageSavedCallback {
////                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
////                    // Image capture success
//////                    val uri=file.toUri().toString()
////                    saveImageToInternalStorage(context ,file.toUri())
////
////                    Log.i(TAG, "The image has been saved in ${file.toUri()}")
////                    // Access the saved image file using outputFileResults.savedUri
////                }
////
////                override fun onError(exception: ImageCaptureException) {
////                    // Handle image capture errors
////                }
////            })
//
//    fun saveImageToInternalStorage(context: Context, imageUri: Uri) {
//        val contentResolver: ContentResolver = context.contentResolver
//
//        // Create a new file in internal storage
//        val internalStorageDir: File = context.filesDir
//        val imageFile = File(internalStorageDir, "my_image.jpg")
//
//        var inputStream: InputStream? = null
//        var outputStream: FileOutputStream? = null
//
//        try {
//            // Retrieve the image data from the URI
//            inputStream = contentResolver.openInputStream(imageUri)
//
//            // Copy the image data to the output file
//            outputStream = FileOutputStream(imageFile)
//            val buffer = ByteArray(4 * 1024) // 4KB buffer
//            var bytesRead: Int
//            while (inputStream!!.read(buffer).also { bytesRead = it } != -1) {
//                outputStream.write(buffer, 0, bytesRead)
//            }
//        } catch (e: IOException) {
//            e.printStackTrace()
//        } finally {
//            try {
//                // Close the input stream and output stream
//                inputStream?.close()
//                outputStream?.close()
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }
//    }
//    @RequiresApi(Build.VERSION_CODES.M)
//    private fun animateFlash() {
//        binding.root.postDelayed({
//            binding.root.foreground = ColorDrawable(Color.WHITE)
//            binding.root.postDelayed({
//                binding.root.foreground = null
//            }, 50)
//        }, 100)
//    }
//
//    companion object {
//        val TAG = "MainActivity"
//    }
//}