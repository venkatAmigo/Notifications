package com.example.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class NotificationReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action ==  "ACTION_REPLY"){
            Toast.makeText(context, "Hello", Toast.LENGTH_SHORT).show()
        }
    }
}