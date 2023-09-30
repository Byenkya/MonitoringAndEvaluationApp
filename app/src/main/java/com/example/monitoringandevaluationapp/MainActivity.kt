package com.example.monitoringandevaluationapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.monitoringandevaluationapp.data.AppDatabase
import com.example.monitoringandevaluationapp.presentation.CaptureImageScreen
import com.example.monitoringandevaluationapp.presentation.MapViewScreen
import com.example.monitoringandevaluationapp.repository.LocationRepository
import com.example.monitoringandevaluationapp.usecases.LocationViewModel
import dagger.hilt.android.AndroidEntryPoint

class MainActivity : ComponentActivity() {
    lateinit var locationViewModel: LocationViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize LocationDao and LocationRepository (this is pseudo-code)
        val locationDao = AppDatabase.getDatabase(this).locationDao()
        val locationRepository = LocationRepository(locationDao)

        // Initialize the ViewModel
        locationViewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LocationViewModel(locationRepository) as T
            }
        })[LocationViewModel::class.java]

        setContent {
            AppNavigation(locationViewModel)
        }
    }


}

@Composable
fun AppNavigation(locationViewModel: LocationViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "mapView") {
        composable("mapView") {
            MapViewScreen(navController = navController)
        }
        composable("captureImage") {
            CaptureImageScreen(navController = navController, locationViewModel = locationViewModel)
        }
    }
}
