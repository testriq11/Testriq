package com.example.testriq.delete_userdefined_traces_module

import android.content.Context
import android.net.Uri

class MessageHelper(private val context: Context) {

    fun deleteAllMessages() {
        val uri = Uri.parse("content://sms")
        context.contentResolver.delete(uri, null, null)
    }
}