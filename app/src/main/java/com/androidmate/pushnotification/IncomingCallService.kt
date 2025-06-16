package com.androidmate.pushnotification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Person
import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.IBinder
import android.util.Log

class IncomingCallService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val callerName = intent?.getStringExtra("CALLER_NAME") ?: "Incoming Call"

        // ✅ Create the channel
        val channelId = "call_channel_${System.currentTimeMillis()}"
        val soundUri = Uri.parse("android.resource://${packageName}/raw/custom_ringtone")

        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        val channel = NotificationChannel(
            channelId,
            "Incoming Call Channel ",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Channel for incoming call notifications"
            setSound(soundUri, attributes) // ✅ Set custom sound
            enableVibration(true)
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        }

        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)

        // ✅ Your notification code continues...
        val acceptIntent = Intent(this, CallActionReceiver::class.java).apply {
            putExtra("ACTION_TYPE", "ACCEPT")
        }
        val acceptPendingIntent = PendingIntent.getBroadcast(this, 1, acceptIntent, PendingIntent.FLAG_IMMUTABLE)

        val rejectIntent = Intent(this, CallActionReceiver::class.java).apply {
            putExtra("ACTION_TYPE", "REJECT")
        }
        val rejectPendingIntent = PendingIntent.getBroadcast(this, 2, rejectIntent, PendingIntent.FLAG_IMMUTABLE)


        val fullScreenIntent = Intent(this, IncomingCallActivity::class.java)
        fullScreenIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        fullScreenIntent.putExtra("CALLER_NAME", callerName)

        val fullScreenPendingIntent = PendingIntent.getActivity(this, 3, fullScreenIntent, PendingIntent.FLAG_IMMUTABLE)

        val person = Person.Builder().setName(callerName).build()

        val notification = Notification.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setStyle(Notification.CallStyle.forIncomingCall(person, rejectPendingIntent, acceptPendingIntent))
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .setAutoCancel(true)
            .addPerson(person)
            .build()

        startForeground(1001, notification)
        return START_NOT_STICKY
    }



    override fun onBind(intent: Intent?): IBinder? = null
}

