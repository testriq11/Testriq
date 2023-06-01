package com.example.testriq.model

import android.graphics.drawable.Drawable

//data class App(val name: String, val icon: Drawable, var isSelected: Boolean = false)

data class App(val name: String, val packageName: String, val icon: Drawable,var isSelected: Boolean = false)

