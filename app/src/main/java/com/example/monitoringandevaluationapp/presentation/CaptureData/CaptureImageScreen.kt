package com.example.monitoringandevaluationapp.presentation.CaptureData

import android.Manifest
import android.app.Activity

import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import coil.compose.rememberImagePainter
import com.example.monitoringandevaluationapp.R
import com.example.monitoringandevaluationapp.data.LocationEntity
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects
import com.example.monitoringandevaluationapp.usecases.LocationViewModel
import com.google.android.gms.maps.model.LatLng
import java.io.FileOutputStream


const val CAMERA_REQUEST_CODE = 1001

fun Context.findActivity(): AppCompatActivity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is AppCompatActivity) {
            return context
        }
        context = context.baseContext
    }
    return null
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaptureImageScreen(navController: NavController, locationViewModel: LocationViewModel) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        "${context.packageName}.provider",
        file
    )

    // State for controlling dialog visibility
    val showDialog = remember { mutableStateOf(true) }

    var userLocation by remember{ mutableStateOf(LatLng(0.0, 0.0)) }

    DisposableEffect(lifecycleOwner) {
        val observer = Observer<LatLng> { newLocation ->
            userLocation = newLocation
            showDialog.value = userLocation.latitude == 0.0 && userLocation.longitude == 0.0
        }

        locationViewModel.userLocation.observe(lifecycleOwner, observer)

        onDispose {
            locationViewModel.userLocation.removeObserver(observer)
        }
    }


    var capturedImageUri by remember { mutableStateOf<Uri>(Uri.EMPTY) }
    var description by remember { mutableStateOf("") }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        capturedImageUri = uri
        // You can add more logic here, such as saving the image URI and description to a database.
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        } else {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_REQUEST_CODE
            )
        }
    }



    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TopAppBar(
            title = { Text("Capture Image") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        if (capturedImageUri.path?.isNotEmpty() == true) {
            Image(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .border(2.dp, Color.Gray),
                painter = rememberImagePainter(capturedImageUri),
                contentDescription = null
            )
        } else {
            Image(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .border(2.dp, Color.Gray),
                painter = painterResource(id = R.drawable.baseline_camera_alt_24), 
                contentDescription = null
            )
        }

        Button(onClick = {
            Log.d("CaptureImageScreen", "Current stored location: Lat=${userLocation.latitude}, Long=${userLocation.longitude}")
            try {
                val permissionCheckResult =
                    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)

                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                    val isCameraAvailable = context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
                    if (isCameraAvailable) {
                        cameraLauncher.launch(uri)
                    } else  {
                        Toast.makeText(
                            context,
                            "Camera being used bt another program",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } else {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            } catch (e: Exception) {
                Log.e("Launch Camera", "Starting camera failed due to ${e.message}")
            }

        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)) {
            Text("Capture Image")
        }

        TextField(
            value = description,
            onValueChange = { newValue -> description = newValue },
            label = { Text("Description") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Button(onClick = {
            Log.e("Data Captured xx", "saving data failed due to ${capturedImageUri} ${description} ${userLocation}")
            if (capturedImageUri != Uri.EMPTY && description.isNotEmpty()) {
                try {
                    val savedFilePath = saveFileToDownloads(context, capturedImageUri)
                    val locationEntity = LocationEntity(
                        id = 0, // This is autogenerated
                        latitude = userLocation.latitude,
                        longitude = userLocation.longitude,
                        description = description,
                        imagePath = savedFilePath
                    )
                    locationViewModel.saveLocation(locationEntity)
                    Toast.makeText(
                        context,
                        "Data saved successfully!!",
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.navigate("SavedImages")
                } catch (e: Exception) {
                    Log.e("Data Capture", "saving data failed due to ${e.message}")
                    throw RuntimeException("Capture: ${e.message}")
                }
            } else {
                Toast.makeText(context, "Missing Information", Toast.LENGTH_SHORT).show()
            }
        },
        enabled = !(userLocation.latitude == 0.0 && userLocation.longitude == 0.0), // Add this line
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)) {
            Text("Save")
        }

        if (showDialog.value) {
            AlertDialog(
                onDismissRequest = { /* don't dismiss manually */ },
                title = { Text("Getting Location") },
                text = {
                    // Added a Column to arrange text and spinner
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Please wait. Fetching your location.")
                        Spacer(modifier = Modifier.height(16.dp)) // Add some space
                        CircularProgressIndicator() // Spinner
                    }
                },
                confirmButton = {
                    // Empty to not have any confirm button
                }
            )
        }


    }
}

// Existing Context extension function
fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyy_MM_dd_HH:mm:ss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )

    return image
}

fun saveFileToDownloads(context: Context, uri: Uri): String {
    val timeStamp = SimpleDateFormat("yyyy_MM_dd_HH:mm:ss").format(Date())
    val downloadsPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val newFile = File(downloadsPath, "$timeStamp.jpg")

    val inputStream = context.contentResolver.openInputStream(uri)
    val outputStream = FileOutputStream(newFile)
    inputStream?.copyTo(outputStream)

    inputStream?.close()
    outputStream.close()

    return newFile.absolutePath
}





