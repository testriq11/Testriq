package com.example.testriq.audio_module

import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat

import com.example.testriq.databinding.ActivityAudioRecordedSaveBinding
import com.example.testriq.databinding.ActivityAudioRecordingBinding
import java.io.File
import java.io.IOException
import java.time.LocalDateTime
import java.util.*

class AudioRecordedSave : AppCompatActivity() {
    private lateinit var binding: ActivityAudioRecordedSaveBinding
    lateinit var mr: MediaRecorder
    private var isRecording = false
    private lateinit var filePath: String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioRecordedSaveBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

//        var path:String=Environment.getExternalStorageDirectory().toString()+"/myrec.3gp"
//        filePath = Environment.getExternalStorageDirectory().absolutePath + "/" + title + ".3gp"

        val timestamp = LocalDateTime.now().toString().replace(":", "-")
        val fileName = "audio$timestamp-${Random().nextInt(10000)}.3gp"
//        val file = File(externalMediaDirs.first(), fileName)

        val folder = File(
            Environment.getExternalStorageDirectory().absolutePath, "voice_record1"
        )
        if (!folder.exists()) {
            folder.mkdirs()
        }
        val folderPath = folder.getAbsolutePath()
//        filePath = File(folderPath, "/" + title + ".3gp").toString()
        filePath = File(folderPath, fileName).toString()
        mr = MediaRecorder()
        binding.recordButton.isEnabled = false
        binding.stopButton.isEnabled = false

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        )
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    android.Manifest.permission.RECORD_AUDIO,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ), 111
            )

        binding.recordButton.isEnabled = true

        binding.recordButton.setOnClickListener {

            mr.setAudioSource(MediaRecorder.AudioSource.MIC)
            mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mr.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            mr.setOutputFile(filePath)
            mr.prepare()
            mr.start()
            binding.stopButton.isEnabled = true


        }

        binding.stopButton.setOnClickListener {
            mr.stop()
            binding.recordButton.isEnabled = true
            binding.stopButton.isEnabled = false
Toast.makeText(this,"Audio Save In Voice voice_record1 Folder",Toast.LENGTH_LONG).show()
        }

        binding.deleteSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                deleteFolderFromInternalStorage()
            }
        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            binding.recordButton.isEnabled = true
        }
    }

    private fun deleteFolderFromInternalStorage() {
        val folder = File(
            Environment.getExternalStorageDirectory().absolutePath, "voice_record1"
        )
        if (folder.exists()) {
            val deleted = folder.deleteRecursively()
            if (deleted) {
                Toast.makeText(this,"Folder Deleted Succesfully",Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this,"Failde to  Deleted Succesfully",Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this,"Folder Doesnt Exist",Toast.LENGTH_LONG).show()
        }
    }
}