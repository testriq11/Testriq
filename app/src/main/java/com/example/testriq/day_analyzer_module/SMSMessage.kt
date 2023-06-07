package com.example.testriq.day_analyzer_module

import java.util.*

//data class SMSMessage(
//    val id: String,
//    val sender: String,
//    val message: String,
//    val date: Long
//
//)

data class SMSMessage(
    val body: String,
    val address: String,
    val date: Date

)