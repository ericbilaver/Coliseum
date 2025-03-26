package com.coliseum.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.coliseum.app.ui.theme.ColiseumTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ColiseumTheme {
                NavigationSetup()
            }
        }
    }
}

@Composable
private fun NavigationSetup() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home",
        route = "{action}"
    ) {
        // Define your navigation routes here
        composable("home") {
            Column {
                Text("Home")
                Button(onClick = { navController.navigate("detail") }) {
                    Text("Detail")
                }
            }

        }
        composable("detail") {
            Column {
                Text("Detail")
                Button(onClick = { navController.popBackStack() }) {
                    Text("Go Back")
                }
            }
        }
    }
}