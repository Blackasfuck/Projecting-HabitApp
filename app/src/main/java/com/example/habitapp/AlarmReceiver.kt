package com.example.habitapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val alarmLabel = intent.getStringExtra("alarmLabel") ?: "No Label"
        Log.d("AlarmReceiver", "Alarm triggered: $alarmLabel")

        // Use the AlarmSoundManager to play the sound
        AlarmSoundManager.playAlarm(context) { isPlaying ->
            // Notify the UI via shared state
            AlarmState.isAlarmPlaying.value = isPlaying
        }
    }
}