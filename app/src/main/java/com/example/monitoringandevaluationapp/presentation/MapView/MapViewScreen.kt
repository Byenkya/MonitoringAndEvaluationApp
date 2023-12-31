package com.example.monitoringandevaluationapp.presentation.MapView

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.example.monitoringandevaluationapp.data.LocationEntity
import com.example.monitoringandevaluationapp.usecases.LocationViewModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
@Composable
fun MapViewScreen(navController: NavController, locationViewModel: LocationViewModel) {
    val context = LocalContext.current
    val mapView = rememberMapViewWithLifecycle(context)
    val lifecycleOwner = LocalLifecycleOwner.current

    // Initialize FusedLocationProviderClient
    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    // Observe LiveData and collect it as state
    val locationEntities = remember { mutableStateOf(listOf<LocationEntity>()) }
    val hasSavedLocationsLoaded = remember { mutableStateOf(false) }
    // Kampala default location
    var userLocation by remember { mutableStateOf(LatLng(0.2947, 32.5935)) }

    // Declare and initialize the LocationManager
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    // Check if location service is enabled
    val isLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    var isDialogVisible by remember { mutableStateOf(true) }

    // Declare request code for location permission
    val LOCATION_REQUEST_CODE = 1002
    val WRITE_REQUEST_CODE = 1003
    val READ_REQUEST_CODE = 1004

    // Create a location request
    val locationRequest = LocationRequest.create().apply {
        interval = 5000L
        fastestInterval = 2000L
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }


    // Create LocationCallback
    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            p0 ?: return
            for (location in p0.locations) {
                userLocation = LatLng(location.latitude, location.longitude)
                 locationViewModel.updateUserLocation(userLocation)
                Log.d("FusedLocation", "locations updated: ${location.latitude}, ${location.longitude}")
            }
        }
    }

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

    if (!isLocationEnabled) {
        if (isDialogVisible) {
            AlertDialog(
                onDismissRequest = { isDialogVisible = false },
                title = { Text(text = "Location Service is Off") },
                text = { Text("To proceed, please turn on the location services.") },
                confirmButton = {
                    Button(onClick = {
                        // Intent to open location settings
                        context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    }) {
                        Text("Open Settings")
                    }
                },
                dismissButton = {
                    Button(onClick = { isDialogVisible = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

    }

    if (hasLocationPermission) {
        Log.d("Success", "Permission granted")
        // Get last known location
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                userLocation = LatLng(location.latitude, location.longitude)
            }
        }
        // Request location updates
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    } else {
        Log.d("Success", "Permission not granted")
        // Request location permission
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_REQUEST_CODE
        )
    }

    LaunchedEffect(locationViewModel, userLocation) {
        locationViewModel.allLocations.observeForever { newList ->
            locationEntities.value = newList
            mapView.getMapAsync { googleMap ->
                setupGoogleMap(googleMap, userLocation, locationEntities)
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            if (hasLocationPermission) {
                fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            }
        }
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
            try {
                mapView.getMapAsync { googleMap ->
                    // First setup
                    setupGoogleMap(googleMap, userLocation, locationEntities)

                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }

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

fun setupGoogleMap(
    googleMap: GoogleMap,
    userLocation: LatLng,
    locationEntities: MutableState<List<LocationEntity>>
) {
    try {
        googleMap.clear()
        if (locationEntities.value.isNotEmpty()) {
            locationEntities.value.forEach { entity ->
                val latLng = LatLng(entity.latitude, entity.longitude)
                googleMap.addMarker(MarkerOptions().position(latLng).title(entity.projectName))
            }
            googleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    locationEntities.value.first().let {
                        LatLng(it.latitude, it.longitude)
                    },
                    15f
                )
            )
        } else {
            // No saved locations, use user location
            googleMap.addMarker(MarkerOptions().position(userLocation).title("Your Location"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
        }
    }catch (e: Exception) {
        Log.e("loading map view error: ", "saving data failed due to ${e.message}")
    }

}





