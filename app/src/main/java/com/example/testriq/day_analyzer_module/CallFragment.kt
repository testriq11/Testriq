package com.example.testriq.day_analyzer_module

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.testriq.R
import com.example.testriq.databinding.FragmentCallBinding
import com.example.testriq.databinding.FragmentMessageBinding


class CallFragment : Fragment() {
    lateinit var binding: FragmentCallBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCallBinding.inflate(inflater,container,false);
        val view = binding.root;
        return view;
    }


}