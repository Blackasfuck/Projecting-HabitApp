package com.example.habitapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TodoListScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Under Development message in the center
        Text(
            text = "Under Development",
            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 32.sp, color = Color.Gray),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
