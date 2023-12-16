 package com.example.monitoringandevaluationapp.presentation.CaptureData

import android.content.Context
import android.content.ContextWrapper
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.monitoringandevaluationapp.usecases.LocationViewModel
import com.google.android.gms.maps.model.LatLng
import com.maryamrzdh.stepper.Stepper
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

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
    // State for controlling dialog visibility
    val showDialog = remember { mutableStateOf(true) }
    var userLocation by remember{ mutableStateOf(LatLng(0.0, 0.0)) }

    val numberStep = 6 // 6 steps in total
    var currentStep by remember { mutableStateOf(1) }
    val titleList = listOf("Step 1", "Step 2", "Step 3", "Step 4", "Step 5", "Step 6")

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

    Column(
        Modifier
            .fillMaxSize()
            .padding(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp,
                bottom = 55.dp // Set the padding for the bottom here
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TopAppBar(
            title = { Text("Project Information") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        Stepper(
            modifier = Modifier.fillMaxWidth(),
            numberOfSteps = numberStep,
            currentStep = currentStep,
            stepDescriptionList = titleList,
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)) {
            when (currentStep) {
                1 -> StepOne(locationViewModel)
                2 -> StepTwo(locationViewModel)
                3 -> StepThree(locationViewModel)
                4 -> StepFour(locationViewModel)
                5 -> StepFive(locationViewModel)
                6 -> Summary(locationViewModel, navController)

            }
        }

        Row(horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()) {

            Button(
                onClick = { if (currentStep > 1) currentStep-- },
                enabled = currentStep > 1) {
                Text(text = "previous")
            }

            Button(
                onClick = {if (currentStep < numberStep) currentStep++ },
                enabled = currentStep < numberStep) {
                Text(text = "next")
            }

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

 @Composable
 fun ProjectDetails(
     projectTitle: String,
     description: String,
     imageUri: Uri
 ) {
     Column(
         horizontalAlignment = Alignment.CenterHorizontally
     ) {
         Text("Save captured Information", fontSize = 20.sp)
         Text(
             text = "Project Title",
             fontSize = 18.sp,
             fontWeight = FontWeight.Bold,
             modifier = Modifier.padding(bottom = 8.dp)
         )

         Text(
             text = projectTitle,
             fontSize = 16.sp,
             modifier = Modifier.padding(bottom = 8.dp)
         )
         Text(
             text = "Description",
             fontSize = 18.sp,
             fontWeight = FontWeight.Bold,
             modifier = Modifier.padding(bottom = 16.dp)
         )
         Text(
             text = description,
             fontSize = 16.sp,
             modifier = Modifier.padding(bottom = 16.dp)
         )

         if (imageUri?.path?.isNotEmpty() == true) {
             Image(
                 painter = rememberImagePainter(imageUri),
                 contentDescription = null,
                 contentScale = ContentScale.Crop,
                 modifier = Modifier
                     .fillMaxWidth()
                     .height(200.dp)
                     .clip(RoundedCornerShape(8.dp))
                     .border(2.dp, Color.Gray)
                     .padding(bottom = 16.dp)
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
    val timeStamp = SimpleDateFormat("yyyy_MM_dd_HH:mm:ss", Locale.getDefault()).format(Date())
    val uniqueIdentifier = UUID.randomUUID().toString()
    val downloadsPath = context.getExternalFilesDir(null)
    val newFileName = "$timeStamp$uniqueIdentifier.jpg"
    val newFile = File(downloadsPath, newFileName)

    val inputStream = context.contentResolver.openInputStream(uri)
    val outputStream = FileOutputStream(newFile)
    inputStream?.copyTo(outputStream)

    inputStream?.close()
    outputStream.close()

    return newFile.absolutePath
}








