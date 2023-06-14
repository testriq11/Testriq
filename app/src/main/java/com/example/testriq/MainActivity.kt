package com.example.testriq

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.CarrierConfigManager
import com.example.testriq.activity_recorder_module.ActivityRecorderActivity
import com.example.testriq.audio_module.AudioRecording
import com.example.testriq.camera_module.Camera
import com.example.testriq.databinding.ActivityMainBinding
import com.example.testriq.day_analyzer_module.DayAnalyzerActivity
import com.example.testriq.delete_userdefined_traces_module.DeleteTracesActivity
import com.example.testriq.fakescreen_module.FakeScreenActivity
import com.example.testriq.gps_module.GpsActivity
import com.example.testriq.screen_capture_module.ScreenCaptureActivity
import com.example.testriq.screen_capture_module.text_to_email.TextForwardEmailActivity

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toggleScreen()
        txtScreen()


    }

    private fun txtScreen() {
        binding.tvAudioRecording.setOnClickListener  {
                val intent= Intent(this,AudioRecording::class.java)
                startActivity(intent)
            }

        binding.tvCamera.setOnClickListener  {
            val intent= Intent(this,Camera::class.java)
            startActivity(intent)
        }

        binding.tvGPS.setOnClickListener  {
            val intent= Intent(this,GpsActivity::class.java)
            startActivity(intent)
        }
        binding.tvFakeScreen.setOnClickListener  {
            val intent= Intent(this,FakeScreenActivity::class.java)
            startActivity(intent)
        }
        binding.tvDeleteTraces.setOnClickListener  {
            val intent= Intent(this,DeleteTracesActivity::class.java)
            startActivity(intent)
        }
        binding.tvDayAnalyzer.setOnClickListener  {
            val intent= Intent(this,DayAnalyzerActivity::class.java)
            startActivity(intent)
        }

        binding.tvTextMsgEmail.setOnClickListener  {
            val intent= Intent(this, TextForwardEmailActivity::class.java)
            startActivity(intent)
        }

        binding.tvscreenCapture.setOnClickListener  {
            val intent= Intent(this,ScreenCaptureActivity::class.java)
            startActivity(intent)
        }

        binding.tvActivityRecorder.setOnClickListener  {
            val intent= Intent(this,ActivityRecorderActivity::class.java)
            startActivity(intent)
        }

        }

    private fun toggleScreen(){
        binding.switchAudio.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked){

                val intent= Intent(this,AudioRecording::class.java)
                startActivity(intent)
            }
            else{

            }
        }

        binding.switchCamera.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked){

                val intent= Intent(this, Camera::class.java)
                startActivity(intent)
            }
            else{

            }
        }

        binding.switchGPS.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked){

                val intent= Intent(this, GpsActivity::class.java)
                startActivity(intent)
            }
            else{

            }
        }

        binding.switchFakeScreen.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked){

                val intent= Intent(this,FakeScreenActivity::class.java)
                startActivity(intent)
            }
            else{

            }
        }


        binding.switchDeleteTraces.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked){

                val intent= Intent(this,DeleteTracesActivity::class.java)
                startActivity(intent)
            }
            else{

            }
        }

        binding.switchDayAnalyzer.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked){

                val intent= Intent(this,DayAnalyzerActivity::class.java)
                startActivity(intent)
            }
            else{

            }
        }

        binding.switchTextMsgEmail.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked){

                val intent= Intent(this, TextForwardEmailActivity::class.java)
                startActivity(intent)
            }
            else{

            }
        }

        binding.switchscreenCapture.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked){

                val intent= Intent(this,ScreenCaptureActivity::class.java)
                startActivity(intent)
            }
            else{

            }
        }

        binding.switchActivityRecorder.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked){

                val intent= Intent(this,ActivityRecorderActivity::class.java)
                startActivity(intent)
            }
            else{

            }
        }


    }
}