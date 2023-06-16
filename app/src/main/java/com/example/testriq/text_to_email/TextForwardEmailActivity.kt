package com.example.testriq.text_to_email

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.testriq.databinding.ActivityTextForwardEmailBinding

class TextForwardEmailActivity : AppCompatActivity() {
    lateinit var binding:ActivityTextForwardEmailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityTextForwardEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}