package com.androidmate.pushnotification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class CallActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val actionType = intent?.getStringExtra("ACTION_TYPE")
        Log.d("CallActionReceiver", "Action received: $actionType")

        when (actionType) {
            "ACCEPT" -> {
                // Handle accept logic here
                Log.d("CallActionReceiver", "Call accepted")
            }
            "REJECT" -> {
                // Handle reject logic here
                Log.d("CallActionReceiver", "Call rejected")
            }
        }

        // Stop the service and remove the notification
        val stopIntent = Intent(context, IncomingCallService::class.java)
        context?.stopService(stopIntent)
    }
}