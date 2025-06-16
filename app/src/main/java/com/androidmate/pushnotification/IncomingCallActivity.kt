package com.androidmate.pushnotification

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class IncomingCallActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Show over lock screen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }

        // Optional: dismiss keyguard (if needed)
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            keyguardManager.requestDismissKeyguard(this, null)
        }

        setContentView(R.layout.activity_incoming_call)

        val answerButton = findViewById<Button>(R.id.answerButton)
        val declineButton = findViewById<Button>(R.id.declineButton)

        answerButton.setOnClickListener {
            Log.d("IncomingCallActivity", "Accepted from UI")
            handleCallAction("ACCEPT")
        }

        declineButton.setOnClickListener {
            Log.d("IncomingCallActivity", "Rejected from UI")
            handleCallAction("REJECT")
        }
    }

    private fun handleCallAction(action: String) {
        // Optional: Do something with the action
        Log.d("IncomingCallActivity", "Call action: $action")

        // ✅ Stop the service and finish the activity
        val stopIntent = Intent(this, IncomingCallService::class.java)
        stopService(stopIntent)

        // Close the UI
        finish()
    }
}
