package com.example.monitoringandevaluationapp.presentation

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import android.Manifest
import android.app.Activity
import android.location.Location
import android.location.LocationManager
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

@Composable
fun MapViewScreen(navController: NavController) {
    val context = LocalContext.current
    val mapView = rememberMapViewWithLifecycle(context)

    // Declare request code for location permission
    val LOCATION_REQUEST_CODE = 1002
    val WRITE_REQUEST_CODE = 1003
    val READ_REQUEST_CODE = 1004

    // Permission check for location
    val hasLocationPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    val hasWritePermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    if(!hasWritePermission) {
        // Request write permission
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            WRITE_REQUEST_CODE
        )
    }

    val hasReadPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    if(!hasReadPermission) {
        // Request read permission
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            READ_REQUEST_CODE
        )
    }


    // Location Manager
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    var userLocation by remember { mutableStateOf(LatLng(0.0, 0.0)) }

    if (hasLocationPermission) {
        val location: Location? = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (location != null) {
            userLocation = LatLng(location.latitude, location.longitude)
        }
    } else {
        // Request location permission
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_REQUEST_CODE
        )
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AndroidView(
            factory = { mapView },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { mapView ->
            mapView.getMapAsync { googleMap ->
                // Enable location
                if (hasLocationPermission) {
                    googleMap.isMyLocationEnabled = true
                }

                // Add a marker and move the camera
                googleMap.addMarker(MarkerOptions().position(userLocation).title("Your Location"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 10f))
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Button(
            onClick = { navController.navigate("captureImage") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(6.dp)
        ) {
            Text("Go to Capture Image")
        }
    }
}


@Composable
fun rememberMapViewWithLifecycle(context: Context): MapView {
    val mapView = remember {
        MapView(context).apply {
            id = android.R.id.content
            onCreate(Bundle())
        }
    }

    // Handle MapView lifecycle
    DisposableEffect(Unit) {
        mapView.onStart()
        mapView.onResume()

        onDispose {
            mapView.onPause()
            mapView.onStop()
            mapView.onDestroy()
        }
    }

    return mapView
}
