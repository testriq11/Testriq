package com.example.testriq

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.example.testriq.databinding.ActivitySplashBinding

class Splash : AppCompatActivity() {

    lateinit var binding:ActivitySplashBinding
    private val SPLASH_DELAY: Long = 2000 // 2 seconds
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.apply {
            systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
        supportActionBar?.hide()
        setContentView(R.layout.activity_splash)

        // Delay for the splash screen
        Handler().postDelayed({
            // Start the main activity after the splash delay
            val intent = Intent(this@Splash, MainActivity::class.java)
            startActivity(intent)
            finish() // Close the splash screen activity
        }, SPLASH_DELAY)

    }
}