package com.example.habitapp

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import java.util.*

@Composable
fun AlarmScreen(navController: NavHostController, context: Context = LocalContext.current) {
    val alarmManager = remember { Alarm(context) }
    val alarms = remember { mutableStateListOf<Alarm.AlarmData>() }
    var showPermissionDialog by remember { mutableStateOf(false) }
    var isAlarmPlaying by remember { mutableStateOf(false) } // Shared state for alarm playing

    // Load existing alarms
    LaunchedEffect(Unit) {
        alarms.clear()
        alarms.addAll(alarmManager.getAlarms())
    }

    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDialog = false },
            title = { Text("Permission Required") },
            text = { Text("This app needs alarm permission to schedule alarms. Please enable it in Settings.") },
            confirmButton = {
                Button(onClick = {
                    showPermissionDialog = false
                    alarmManager.openAlarmSettings()
                }) {
                    Text("Open Settings")
                }
            },
            dismissButton = {
                Button(onClick = { showPermissionDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Alarm Manager", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(alarms) { alarm ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Time: ${Date(alarm.timeInMillis)}")
                            Text("Label: ${alarm.label}")
                        }
                        Button(onClick = {
                            alarmManager.deleteAlarm(alarm.id)
                            alarms.removeAll { it.id == alarm.id }
                        }) {
                            Text("Delete")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (alarmManager.checkAlarmPermission()) {
                val time = System.currentTimeMillis() + 10 * 1000 // 10 seconds later
                val newAlarm = alarmManager.addAlarm(time, "Test Alarm")
                if (newAlarm != null) {
                    alarms.add(newAlarm)
                }
            } else {
                showPermissionDialog = true
            }
        }) {
            Text("Add Alarm")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Always show the Stop Alarm button
        Button(onClick = {
            // Ensure we stop the alarm sound here
            if (isAlarmPlaying) {
                AlarmSoundManager.stopAlarm { isPlaying ->
                    isAlarmPlaying = isPlaying
                }
            }
        }) {
            Text("Stop Alarm")
        }
    }
}

class Alarm(private val context: Context) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val alarms = mutableListOf<AlarmData>()

    data class AlarmData(
        val id: Int,
        val timeInMillis: Long,
        val label: String
    )

    fun checkAlarmPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }

    fun openAlarmSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val intent = Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    fun addAlarm(timeInMillis: Long, label: String): AlarmData? {
        if (!checkAlarmPermission()) {
            Toast.makeText(context, "Alarm permission not granted", Toast.LENGTH_SHORT).show()
            return null
        }

        try {
            Log.d("Alarm", "Adding alarm for $label at $timeInMillis")
            val id = (alarms.maxOfOrNull { it.id } ?: 0) + 1
            val alarm = AlarmData(id, timeInMillis, label)
            alarms.add(alarm)

            val intent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra("alarmId", alarm.id)
                putExtra("alarmLabel", alarm.label)
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                alarm.id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
                }
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
            }

            Log.d("Alarm", "Alarm set for ${Date(timeInMillis)}")
            return alarm
        } catch (e: Exception) {
            Log.e("Alarm", "Error setting alarm", e)
            Toast.makeText(context, "Error setting alarm: ${e.message}", Toast.LENGTH_SHORT).show()
            return null
        }
    }

    fun deleteAlarm(id: Int) {
        val alarm = alarms.find { it.id == id } ?: return
        alarms.remove(alarm)

        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        pendingIntent?.let {
            alarmManager.cancel(it)
            it.cancel()
        }

        Toast.makeText(context, "Alarm deleted", Toast.LENGTH_SHORT).show()
    }

    fun getAlarms(): List<AlarmData> = alarms.toList()
}