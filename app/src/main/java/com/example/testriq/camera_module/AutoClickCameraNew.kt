package com.example.testriq.camera_module

import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.media.Image
import android.media.ImageReader
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.example.testriq.R
import com.example.testriq.databinding.ActivityAutoClickCameraBinding
import com.example.testriq.databinding.ActivityAutoClickCameraNewBinding
import java.io.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList


class AutoClickCameraNew : AppCompatActivity() {
    private lateinit var runnable: Runnable
    lateinit var binding: ActivityAutoClickCameraNewBinding
    private lateinit var textureView: TextureView
    private lateinit var cameraManager: CameraManager
    private var cameraDevice: CameraDevice? = null
    var file=""
    private var cameraCaptureSession: CameraCaptureSession? = null
    private var cameraId: String? = null
    private val cameraStateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            cameraDevice = camera
            createCameraPreviewSession()
        }

        override fun onDisconnected(camera: CameraDevice) {
            cameraDevice?.close()
            cameraDevice = null
        }

        override fun onError(camera: CameraDevice, error: Int) {
            cameraDevice?.close()
            cameraDevice = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAutoClickCameraNewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        textureView = findViewById(R.id.textureView)
        cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager

        textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
                openCamera()
            }

            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
                // Handle texture size change, if needed.
            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                return true
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
                // Handle texture updated, if needed.
            }
        }

//
    }

    private fun openCamera() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            try {
                cameraId = cameraManager.cameraIdList[0]
                cameraManager.openCamera(cameraId!!, cameraStateCallback, null)

            } catch (e: CameraAccessException) {
                // Handle camera access exception.
            }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
        }
    }

    private fun createCameraPreviewSession() {
        val surfaceTexture = textureView.surfaceTexture
        surfaceTexture?.setDefaultBufferSize(textureView.width, textureView.height)
        val surface = Surface(surfaceTexture)

        try {
            val captureRequestBuilder = cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            captureRequestBuilder?.addTarget(surface)

            cameraDevice?.createCaptureSession(listOf(surface),
                object : CameraCaptureSession.StateCallback() {
                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onConfigured(session: CameraCaptureSession) {
                        cameraCaptureSession = session
                        captureRequestBuilder?.build()?.let { captureRequest ->
                            cameraCaptureSession?.setRepeatingRequest(captureRequest, null, null)
                            captureImage()
                        }
                    }

                    override fun onConfigureFailed(session: CameraCaptureSession) {
                        // Handle camera configuration failure.
                    }
                     }, null)
        } catch (e: CameraAccessException) {
            // Handle camera access exception.
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCamera()
        }
    }

    override fun onPause() {
        super.onPause()

        cameraDevice?.close()
        cameraDevice = null
        cameraCaptureSession?.close()
        cameraCaptureSession = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun captureImage() {
        var capturedImageCount = 0
        val desiredImageCount = 5
        var captureCount = 0
        var timer = Timer()
        var  totalCaptures= 12

        var intervalMillis=5000L

        try {


                val manager = getSystemService(CAMERA_SERVICE) as CameraManager
                val characteristics = cameraDevice?.let { manager.getCameraCharacteristics(it.id) }
                val jpegSizes: Array<Size>? =
                    characteristics?.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                        ?.getOutputSizes(ImageFormat.JPEG)

                // Capture image with custom size
                var width = 640
                var height = 480
                if (jpegSizes != null && jpegSizes.isNotEmpty()) {
                    width = jpegSizes[0].width
                    height = jpegSizes[0].height
                }
                val reader: ImageReader =
                    ImageReader.newInstance(width, height, ImageFormat.JPEG, 1)
                val outputSurfaces: MutableList<Surface> = ArrayList(2)
                outputSurfaces.add(reader.surface)
                outputSurfaces.add(Surface(textureView.surfaceTexture))
                val captureBuilder: CaptureRequest.Builder =
                    cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
                captureBuilder.addTarget(reader.surface)
                captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)

                // Check rotation
                val rotation = windowManager.defaultDisplay.rotation
                captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, rotation)

                val timestamp = LocalDateTime.now().toString().replace(":", "-")
                val fileName = "img$timestamp-${Random().nextInt(10000)}.jpg"
                val file = File(externalMediaDirs.first(), fileName)

//            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
//            val imageFileName = "IMG_" + timeStamp + ".jpg"
//            file = File(getExternalFilesDir(null), imageFileName).toString()
                val readerListener: ImageReader.OnImageAvailableListener =
                    object : ImageReader.OnImageAvailableListener {
                        override fun onImageAvailable(reader: ImageReader) {
                            var image: Image? = null
                            try {
                                image = reader.acquireLatestImage()
                                val buffer = image.planes[0].buffer
                                val bytes = ByteArray(buffer.capacity())
                                buffer.get(bytes)
                                save(bytes)
                            } catch (e: FileNotFoundException) {
                                e.printStackTrace()
                            } catch (e: IOException) {
                                e.printStackTrace()
                            } finally {
                                image?.close()
                            }
                        }

                        @Throws(IOException::class)
                        private fun save(bytes: ByteArray) {
                            var output: OutputStream? = null
                            try {
                                output = FileOutputStream(file)
                                output.write(bytes)
                            } finally {
                                output?.close()
                            }
                        }
                    }
                reader.setOnImageAvailableListener(readerListener, null)
                val captureListener: CameraCaptureSession.CaptureCallback =
                    object : CameraCaptureSession.CaptureCallback() {
                        override fun onCaptureCompleted(
                            session: CameraCaptureSession,
                            request: CaptureRequest,
                            result: TotalCaptureResult
                        ) {

                            super.onCaptureCompleted(session, request, result)
                            Toast.makeText(
                                this@AutoClickCameraNew,
                                "Image Saved",
                                Toast.LENGTH_SHORT
                            ).show()
                            capturedImageCount++
                            if (capturedImageCount >= desiredImageCount) {
                                session.close() // Close the camera capture session
                            } else {
                                createCameraPreviewSession() // Continue capturing if desired count is not reached
                            }

                        }
                    }
                cameraDevice!!.createCaptureSession(
                    outputSurfaces,
                    object : CameraCaptureSession.StateCallback() {
                        override fun onConfigured(session: CameraCaptureSession) {
                            try {
                                session.capture(captureBuilder.build(), captureListener, null)
                            } catch (e: CameraAccessException) {
                                e.printStackTrace()
                            }
                        }

                        override fun onConfigureFailed(session: CameraCaptureSession) {
                        }
                    },
                    null
                )
             } catch (e: CameraAccessException) {
            e.printStackTrace()
        }


    }



    private fun closeCamera() {
        cameraDevice?.close()
        cameraDevice = null
    }

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 100

        init {
//            ORIENTATIONS.append(Surface.ROTATION_0, 90)
//            ORIENTATIONS.append(Surface.ROTATION_90, 0)
//            ORIENTATIONS.append(Surface.ROTATION_180, 270)
//            ORIENTATIONS.append(Surface.ROTATION_270, 180)
        }
    }

}