package com.example.testriq.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.testriq.day_analyzer_module.CallFragment
import com.example.testriq.day_analyzer_module.MessageFragment

class DayAnalyzerPagerAdapter (fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int {
        return 2// Number of tabs
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> MessageFragment()
            1 -> CallFragment()
//            2 -> Lo()
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Message"
            1 -> "Calls"
//            2 -> "Locations"
            else -> null
        }
    }
}