package com.example.habitapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.habitapp.ui.theme.HabitAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HabitAppTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) { innerPadding ->
        NavigationHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    NavigationBar(
        containerColor = Color(0x00212121),
        contentColor = Color.White
    ) {
        NavigationBarItem(
            selected = navController.currentBackStackEntry?.destination?.route == "main_menu",
            onClick = { navController.navigate("main_menu") },
            label = { Text("Menu") },
            icon = { Icon(Icons.Default.Home, contentDescription = "Menu") }
        )
        NavigationBarItem(
            selected = navController.currentBackStackEntry?.destination?.route == "habits",
            onClick = { navController.navigate("habits") },
            label = { Text("Habits") },
            icon = { Icon(Icons.Default.List, contentDescription = "Habits") }
        )
        NavigationBarItem(
            selected = navController.currentBackStackEntry?.destination?.route == "others",
            onClick = { navController.navigate("others") },
            label = { Text("Others") },
            icon = { Icon(Icons.Default.Info, contentDescription = "Others") }
        )
    }
}

@Composable
fun NavigationHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = "main_menu",
        modifier = modifier
    ) {
        composable("main_menu") {
            Menu().Content(navController)
        }

        composable("habits") {
            HabitsScreen(navController)
        }

        composable("others") {
            OthersScreen()
        }

        composable("alarm") {
            AlarmScreen(navController = navController)
        }

        composable("game1") {
            BlockBlastGame()
        }

        composable("game2") {
            SnakeGame()
        }

        composable("list") {
            TodoListScreen()
        }

        // Web pages from Menu
        Menu().getWebPages().forEach { page ->
            composable(page.route) {
                WebViewScreen(url = page.url)
            }
        }
    }
}