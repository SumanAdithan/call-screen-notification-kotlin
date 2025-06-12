package com.androidmate.pushnotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.UUID

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("FCM", "Message Received: ${remoteMessage.data}")

        val title = remoteMessage.data["title"]
        val body = remoteMessage.data["body"]
        sendNotification(title, body)
    }

    override fun onNewToken(token: String) {
        Log.d("FCM", "Refreshed token: $token")
        // Send token to your server if needed
    }

    private fun sendNotification(title: String?, message: String?) {
        val intent = Intent(this, IncomingCallService::class.java).apply {
            putExtra("CALLER_NAME", title ?: "Incoming Call")
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

}