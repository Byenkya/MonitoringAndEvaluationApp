package com.example.monitoringandevaluationapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.monitoringandevaluationapp.data.AppDatabase
import com.example.monitoringandevaluationapp.presentation.CaptureImageScreen
import com.example.monitoringandevaluationapp.presentation.MapViewScreen
import com.example.monitoringandevaluationapp.presentation.SavedImageList
import com.example.monitoringandevaluationapp.repository.LocationRepository
import com.example.monitoringandevaluationapp.usecases.LocationViewModel
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

class MainActivity : ComponentActivity() {
    lateinit var locationViewModel: LocationViewModel

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
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
            }
        )[LocationViewModel::class.java]

        setContent {
            val navController = rememberNavController()

            Scaffold(
                bottomBar = {
                    AppBottomNavigation(navController)
                }
            ) {
                AppNavigation(navController, locationViewModel)
            }
        }
    }
}

@Composable
fun AppBottomNavigation(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BottomNavigation (backgroundColor = Color(0xFF6200EA)) {
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Home, contentDescription = null, tint = Color.White) },
            label = { Text("Map View", color = Color.White) },
            selected = currentRoute == "mapView",
            onClick = {
                if (currentRoute != "mapView") {
                    navController.navigate("mapView")
                }
            }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.White) },
            label = { Text("Capture Data", color = Color.White) },
            selected = currentRoute == "captureImage",
            onClick = {
                if (currentRoute != "captureImage") {
                    navController.navigate("captureImage")
                }
            }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Info, contentDescription = null, tint = Color.White) },
            label = { Text("Saved Data", color = Color.White) },
            selected = currentRoute == "SavedImages",
            onClick = {
                if (currentRoute != "SavedImages") {
                    navController.navigate("SavedImages")
                }
            }
        )
        // Add other BottomNavigationItems here
    }
}

@Composable
fun AppNavigation(navController: NavHostController, locationViewModel: LocationViewModel) {
    NavHost(navController = navController, startDestination = "mapView") {
        composable("mapView") {
            MapViewScreen(navController = navController)
        }
        composable("captureImage") {
            CaptureImageScreen(navController = navController, locationViewModel = locationViewModel)
        }
        composable("SavedImages") {
            SavedImageList(navController = navController, viewModel = locationViewModel)
        }
    }
}
