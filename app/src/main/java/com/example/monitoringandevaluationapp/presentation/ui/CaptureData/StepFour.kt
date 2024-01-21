package com.example.monitoringandevaluationapp.presentation.ui.CaptureData

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import coil.compose.rememberAsyncImagePainter
import com.example.monitoringandevaluationapp.R
import com.example.monitoringandevaluationapp.data.Dates
import com.example.monitoringandevaluationapp.presentation.ViewModel.LocationViewModel
import com.google.android.gms.maps.model.LatLng
import java.util.Objects

@Composable
fun StepFour(locationViewModel: LocationViewModel) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var userLocation by remember{ mutableStateOf(LatLng(0.0, 0.0)) }
    val assessmentDate = remember { mutableStateOf("") }
    val assessedBy by locationViewModel.assessedBy.collectAsState()
    val assessMilestones by locationViewModel.assessMilestone.collectAsState()
    val assessmentFor by locationViewModel.assessmentFor.collectAsState()
    val obs by locationViewModel.obs.collectAsState()
    val photoOneUri by locationViewModel.photoOneUri.collectAsState()
    val photoTwoUri by locationViewModel.photoTwoUri.collectAsState()
    val photoThreeUri by locationViewModel.photoThreeUri.collectAsState()
    val photoFourUri by locationViewModel.photoFourUri.collectAsState()
    val lat by locationViewModel.lat.collectAsState()
    val long by locationViewModel.long.collectAsState()
    val altitude by locationViewModel.altitude.collectAsState()
    val gps by locationViewModel.gps.collectAsState()
    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        "${context.packageName}.provider",
        file
    )
    val cameraLauncher1 = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        locationViewModel.updatePhotoOneUri(uri)
    }

    val cameraLauncher2 = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        locationViewModel.updatePhotoTwoUri(uri)
    }

    val cameraLauncher3 = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        locationViewModel.updatePhotoThreeUri(uri)
    }

    val cameraLauncher4 = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        locationViewModel.updatePhotoFourUri(uri)
    }


    DisposableEffect(lifecycleOwner) {
        val observer = Observer<LatLng> { newLocation ->
            userLocation = newLocation
        }

        locationViewModel.userLocation.observe(lifecycleOwner, observer)

        onDispose {
            locationViewModel.userLocation.removeObserver(observer)
        }
    }

    Column(
        Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
    ) {
        Text("Assessment Information", fontSize = 18.sp)
        Divider(color = Color.Black, thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))

        // assessmentDate
        DateFieldWithPicker(
            label = "Assessment Date",
            dates = Dates,
            date = assessmentDate,
            onDateSelected = { date ->
                locationViewModel.updateAssessmentDate(date)
            }
        )

        // AssessedBy
        TextField(
            value = assessedBy,
            onValueChange = { locationViewModel.updateAssessedBy(it) },
            label = { Text("Assessed By") },
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )

        // assess milestone
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Assess Milestone")
            Spacer(modifier = Modifier.width(4.dp))
            Checkbox(
                checked = assessMilestones,
                onCheckedChange = { checked ->
                    locationViewModel.updateAssessMilestone(checked)
                }
            )
        }

        // assessment for
        AssessmentForRadioGroup(
            selectedAssessmentFor = assessmentFor,
            onAssessmentForSelected = { assessmentFor -> locationViewModel.updateAssessmentFor(assessmentFor) }
        )

        // observation
        TextField(
            value = obs,
            onValueChange = { locationViewModel.updateObs(it) },
            label = { Text("Observation") },
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )

        //photo 1
        ImageCaptureButton(
            title = "Photo 1",
            context = context,
            cameraLauncher = cameraLauncher1,
            uri = uri,
            locationViewModel = locationViewModel,
            imageUri = photoOneUri,
            imageUriUpdater = { updatedUri -> locationViewModel.updatePhotoOneUri(updatedUri) }
        )

        // photo 2
        ImageCaptureButton(
            title = "Photo 2",
            context = context,
            cameraLauncher = cameraLauncher2,
            uri = uri,
            locationViewModel = locationViewModel,
            imageUri = photoTwoUri,
            imageUriUpdater = { updatedUri -> locationViewModel.updatePhotoTwoUri(updatedUri) }
        )

        // photo 3
        ImageCaptureButton(
            title = "Photo 3",
            context = context,
            cameraLauncher = cameraLauncher3,
            uri = uri,
            locationViewModel = locationViewModel,
            imageUri = photoThreeUri,
            imageUriUpdater = { updatedUri -> locationViewModel.updatePhotoThreeUri(updatedUri) }
        )

        // photo 4
        ImageCaptureButton(
            title = "Photo 4",
            context = context,
            cameraLauncher = cameraLauncher4,
            uri = uri,
            locationViewModel = locationViewModel,
            imageUri = photoFourUri ?: Uri.EMPTY,
            imageUriUpdater = { updatedUri -> locationViewModel.updatePhotoFourUri(updatedUri) }
        )

        // lat
        TextField(
            value = userLocation.latitude.toString(),
            onValueChange = { newString ->
                if (newString.isEmpty() || newString.toIntOrNull() != null) {
                    locationViewModel.updateLat(newString.toDouble())
                }
            },
            label = { Text("Latitude") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )

        // long
        TextField(
            value = userLocation.longitude.toString(),
            onValueChange = { newString ->
                if (newString.isEmpty() || newString.toIntOrNull() != null) {
                    locationViewModel.updateLong(newString.toDouble())
                }
            },
            label = { Text("Longitude") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )

        // Altitude
        TextField(
            value = altitude,
            onValueChange = { locationViewModel.updateAltitude(it) },
            label = { Text("Altitude") },
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )

        // Gps
        TextField(
            value = gps,
            onValueChange = { locationViewModel.updateGps(it) },
            label = { Text("GPS CRS") },
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )

    }


}

@Composable
fun AssessmentForRadioGroup(
    selectedAssessmentFor: String,
    onAssessmentForSelected: (String) -> Unit
) {
    val options = listOf("Values", "Inputs", "Others")

    Column {
        Text("Select Assessment For", modifier = Modifier.padding(16.dp))

        options.forEach { assessmentFor ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .selectable(
                        selected = assessmentFor == selectedAssessmentFor,
                        onClick = { onAssessmentForSelected(assessmentFor) }
                    )
            ) {
                RadioButton(
                    selected = assessmentFor == selectedAssessmentFor,
                    onClick = null // null as we're handling selection in the Row's onClick
                )
                Text(
                    text = assessmentFor,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

fun captureImage(
    context: Context,
    cameraLauncher: ActivityResultLauncher<Uri>,
    uri: Uri,
    locationViewModel: LocationViewModel,
    imageUriUpdater: (Uri) -> Unit
) {
    try {
        val permissionCheckResult = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)

        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
            val isCameraAvailable = context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
            if (isCameraAvailable) {
                cameraLauncher.launch(uri)
            } else {
                Toast.makeText(
                    context,
                    "Camera being used by another program",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    } catch (e: Exception) {
        Log.e("Launch Camera", "Starting camera failed due to ${e.message}")
    }
}

@Composable
fun ImageCaptureButton(
    title: String,
    context: Context,
    cameraLauncher: ActivityResultLauncher<Uri>,
    uri: Uri,
    locationViewModel: LocationViewModel,
    imageUri: Uri?,
    modifier: Modifier = Modifier,
    imageUriUpdater: (Uri) -> Unit
) {
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            captureImage(context, cameraLauncher, uri, locationViewModel, imageUriUpdater)
        }
    }


    if (imageUri?.path?.isNotEmpty() == true) {
        Image(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(2.dp, Color.Gray),
            painter = rememberAsyncImagePainter(imageUri),
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

    Button(
        onClick = {
            captureImage(context, cameraLauncher, uri, locationViewModel, imageUriUpdater)
        },
        modifier = modifier.fillMaxWidth()
    ) {
        Text("Capture $title")
    }
}

