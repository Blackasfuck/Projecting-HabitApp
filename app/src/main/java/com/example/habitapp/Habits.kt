package com.example.habitapp

import android.media.MediaPlayer
import android.provider.Settings
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController

@Composable
fun HabitsScreen(navController: NavHostController) {

    var mediaPlayer: MediaPlayer? by remember { mutableStateOf(null) }

    // Function to start the alarm sound
    fun startAlarm() {
        val ringtoneUri = Settings.System.DEFAULT_ALARM_ALERT_URI
        mediaPlayer = MediaPlayer.create(navController.context, ringtoneUri)
        mediaPlayer?.start()
    }

    // Function to stop the alarm sound
    fun stopAlarm() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
                it.release()  // Release the MediaPlayer resources
                mediaPlayer = null
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                SquareButton(
                    iconRes = R.drawable.alarm,
                    color = Color(0xFFFFCD70),
                    onClick = {
                        Log.d("HabitsScreen", "Navigating to Alarm screen")
                        navController.navigate("alarm")
                    },
                    label = "Alarm"
                )
            }
            item {
                SquareButton(
                    iconRes = R.drawable.blockblast,
                    color = Color(0xFF83E0E0),
                    onClick = {
                        Log.d("HabitsScreen", "Navigating to Block Blast game")
                        navController.navigate("game1")
                    },
                    label = "Block Blast"
                )
            }
            item {
                SquareButton(
                    iconRes = R.drawable.snakegame,
                    color = Color(0xFF5AFF93),
                    onClick = {
                        Log.d("HabitsScreen", "Navigating to Snake game")
                        navController.navigate("game2")
                    },
                    label = "Snake"
                )
            }
            item {
                SquareButton(
                    iconRes = R.drawable.todo,
                    color = Color(0xFFF16BF1),
                    onClick = {
                        Log.d("HabitsScreen", "Navigating to TODO list")
                        navController.navigate("list")  // Navigates to TODO List screen
                    },
                    label = "TO-DO List"
                )
            }
        }
    }
}

@Composable
fun GameWebView(url: String) {
    var webView: WebView? by remember { mutableStateOf(null) }

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                webView = this
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    databaseEnabled = true
                }
                webViewClient = WebViewClient()
                loadUrl(url)
            }
        },
        modifier = Modifier.fillMaxSize()
    )

    DisposableEffect(Unit) {
        onDispose {
            webView?.destroy()
        }
    }
}

@Composable
fun BlockBlastGame() {
    Box(modifier = Modifier.fillMaxSize()) {
        GameWebView("https://blockblast.game/") // Replace with your actual game URL
    }
}

@Composable
fun SnakeGame() {
    Box(modifier = Modifier.fillMaxSize()) {
        GameWebView("https://snakegame.org/") // Replace with your actual game URL
    }
}

/*@Composable
fun TodoListScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "TO-DO List",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}*/
@Composable
fun SquareButton(iconRes: Int, color: Color, onClick: () -> Unit, label: String) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .size(150.dp)
            .padding(8.dp)
            .clip(RoundedCornerShape(2.dp)),
        colors = ButtonDefaults.buttonColors(containerColor = color),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = label, style = MaterialTheme.typography.bodyLarge)
        }
    }
}