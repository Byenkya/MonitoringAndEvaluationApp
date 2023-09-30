package com.example.monitoringandevaluationapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.monitoringandevaluationapp.presentation.CaptureImageScreen
import com.example.monitoringandevaluationapp.presentation.MapViewScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation()
        }
    }


}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "mapView") {
        composable("mapView") {
            MapViewScreen(navController = navController)
        }
        composable("captureImage") {
            CaptureImageScreen(navController = navController)
        }
    }
}
