package com.example.monitoringandevaluationapp.presentation.ui.sigin

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun LoginScreen(
    navController: NavController,
    state: SignInState,
    onSignInClick: () -> Unit
) {
    val context = LocalContext.current
    // Declare and initialize the LocationManager
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    // Check if location service is enabled
    val isLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    var isDialogVisible by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let {error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    if (!isLocationEnabled) {
        if (isDialogVisible) {
            AlertDialog(
                onDismissRequest = { isDialogVisible = false },
                title = { androidx.compose.material3.Text(text = "Location Service is Off") },
                text = { androidx.compose.material3.Text("To proceed, please turn on the location services.") },
                confirmButton = {
                    androidx.compose.material3.Button(onClick = {
                        // Intent to open location settings
                        context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    }) {
                        androidx.compose.material3.Text("Open Settings")
                    }
                },
                dismissButton = {
                    androidx.compose.material3.Button(onClick = {
                        isDialogVisible = false
                        navController.navigate("mapView")
                    }) {
                        androidx.compose.material3.Text("Cancel")
                    }
                }
            )
        }

    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = onSignInClick) {
            Text(text = "Sign In")
        }

    }
}