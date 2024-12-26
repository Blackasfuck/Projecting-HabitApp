package com.example.habitapp

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.LaunchedEffect

@Composable
fun OthersScreen() {
    val context = LocalContext.current
    val openLinkUrl = remember { mutableStateOf<String?>(null) }

    // Trigger URL opening when openLinkUrl changes
    LaunchedEffect(openLinkUrl.value) {
        openLinkUrl.value?.let {
            openLink(context, it)
            openLinkUrl.value = null // Reset after opening the link
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Our Team",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TeamMember(
            name = "Zhanibek",
            jobDescription = "Team Lead, Scrum Master",
            instagramLink = "https://www.instagram.com/_zhanibek.a_?utm_source=ig_web_button_share_sheet&igsh=ZDNlZDc0MzIxNw==",
            imageRes = R.drawable.zhoni,
            onInstagramClick = { openLinkUrl.value = it }
        )

        TeamMember(
            name = "Sandugash",
            jobDescription = "UI & UX Designer",
            instagramLink = "https://www.instagram.com/maratovva.s?utm_source=ig_web_button_share_sheet&igsh=ZDNlZDc0MzIxNw==",
            imageRes = R.drawable.sandu,
            onInstagramClick = { openLinkUrl.value = it }
        )

        TeamMember(
            name = "Qanat",
            jobDescription = "Lead Developer",
            instagramLink = "https://www.instagram.com/bratishka_momonga_?utm_source=ig_web_button_share_sheet&igsh=ZDNlZDc0MzIxNw==",
            imageRes = R.drawable.qana,
            onInstagramClick = { openLinkUrl.value = it }
        )

        Spacer(modifier = Modifier.height(16.dp))
        Divider(color = Color.Black)

        Button(onClick = {
            openLinkUrl.value = "https://github.com/Blackasfuck/Projecting-HabitApp"
        }) {
            Text("Check our project")
        }

        Divider(color = Color.Black, modifier = Modifier.padding(vertical = 16.dp))

        // Write Review Button
        Button(onClick = {
            openLinkUrl.value = "https://forms.office.com/r/8yEXPvYikw"
        }) {
            Text("Write a review")
        }
    }
}

@Composable
fun TeamMember(name: String, jobDescription: String, instagramLink: String, imageRes: Int, onInstagramClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = name, style = MaterialTheme.typography.bodyLarge)
            Text(text = jobDescription, style = MaterialTheme.typography.bodyMedium)
            Button(onClick = { onInstagramClick(instagramLink) }) {
                Text("View Instagram")
            }
        }
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = name,
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(12.dp)) // Rounded corners
                .padding(4.dp)
        )
    }
}

fun openLink(context: android.content.Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}