package com.example.habitapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.habitapp.ui.theme.HabitAppTheme

data class WebPage(
    val route: String,
    val name: String,
    val url: String
)
class Menu {
    private val webPages = listOf(
//        WebPage(route = "Check our Progress", name = "Check our Progress", url = "webview/https://trello.com/b/PzVrsSA2/habit-app")/*,
        WebPage(route = "Check our Progress", name = "Check our Progress", url = "https://github.com/Blackasfuck/Projecting-HabitApp")
    )

    @Composable
    fun Content(navController: androidx.navigation.NavController) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.habitapp_logo),
                    contentDescription = "Centered Image",
                    modifier = Modifier
                        .size(400.dp)
                        .padding(bottom = 32.dp)
                )
                webPages.forEach { page ->
                    Button(
                        onClick = { navController.navigate(page.route) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = page.name,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }

    fun getWebPages(): List<WebPage> = webPages
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(url: String) {
    val isLoading = remember { mutableStateOf(true) }

    Column(modifier = Modifier.fillMaxSize()) {
        if (isLoading.value) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            isLoading.value = false
                        }
                    }
                    loadUrl(url)
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}