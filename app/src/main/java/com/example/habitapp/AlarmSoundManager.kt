package com.example.habitapp

import android.content.Context
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.util.Log

object AlarmSoundManager {
    var currentRingtone: Ringtone? = null

    // Play alarm sound and notify state
    fun playAlarm(context: Context, onAlarmPlaying: (Boolean) -> Unit) {
        try {
            val alarmUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            currentRingtone = RingtoneManager.getRingtone(context, alarmUri)
            currentRingtone?.play()

            // Notify the state
            onAlarmPlaying(true)
        } catch (e: Exception) {
            Log.e("AlarmSoundManager", "Error playing alarm sound", e)
        }
    }
    // Stop the alarm sound
    fun stopAlarm(onAlarmPlaying: (Boolean) -> Unit) {
        Log.d("AlarmSoundManager", "Stopping alarm sound")
        currentRingtone?.stop()
        currentRingtone = null
        onAlarmPlaying(false)
    }
}