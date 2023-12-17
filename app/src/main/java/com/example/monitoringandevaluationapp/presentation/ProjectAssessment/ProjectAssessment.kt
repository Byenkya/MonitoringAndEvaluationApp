package com.example.monitoringandevaluationapp.presentation.ProjectAssessment

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.example.monitoringandevaluationapp.data.Dates
import com.example.monitoringandevaluationapp.data.LocationEntity
import com.example.monitoringandevaluationapp.presentation.CaptureData.AssessmentForRadioGroup
import com.example.monitoringandevaluationapp.presentation.CaptureData.DateFieldWithPicker
import com.example.monitoringandevaluationapp.presentation.CaptureData.DropdownWithLabel
import com.example.monitoringandevaluationapp.presentation.CaptureData.ImageCaptureButton
import com.example.monitoringandevaluationapp.presentation.CaptureData.createImageFile
import com.example.monitoringandevaluationapp.usecases.LocationViewModel
import com.google.android.gms.maps.model.LatLng
import java.util.Objects
import androidx.lifecycle.Observer
import com.example.monitoringandevaluationapp.presentation.CaptureData.saveFileToDownloads

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectAssessment(navController: NavController, locationViewModel: LocationViewModel) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var selectedProject by remember { mutableStateOf("") }
    var selectedAssessmentPerson by remember { mutableStateOf("") }
    val projects = remember { mutableStateListOf<LocationEntity>() }
    var selectedProjectDetails by remember { mutableStateOf<LocationEntity?>(null) }
    val assessmentDate = remember { mutableStateOf("") }
    val assessMilestones by locationViewModel.assessMilestone.collectAsState()
    val assessmentFor by locationViewModel.assessmentFor.collectAsState()
    val obs by locationViewModel.obs.collectAsState()
    val photoOneUri by locationViewModel.photoOneUri.collectAsState()
    val photoTwoUri by locationViewModel.photoTwoUri.collectAsState()
    val photoThreeUri by locationViewModel.photoThreeUri.collectAsState()
    val photoFourUri by locationViewModel.photoFourUri.collectAsState()
    var userLocation by remember{ mutableStateOf(LatLng(0.0, 0.0)) }

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

    LaunchedEffect(key1 = locationViewModel.allLocations) {
        locationViewModel.allLocations.observeForever { newList ->
            projects.clear()
            projects.addAll(newList)
        }
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
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start
    ) {
        TopAppBar(
            title = { Text("Project Assessment", fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        // project name drop down
        DropdownWithLabel(
            label = "Select Project",
            suggestions = projects.map { it.projectName }.distinct(),
            selectedText = selectedProject,
            onTextSelected = { projectName ->
                selectedProject = projectName
                selectedProjectDetails = projects.firstOrNull { it.projectName == projectName }
            }
        )

        // Assessment Date
        DateFieldWithPicker(
            label = "Assessment Date",
            dates = Dates,
            date = assessmentDate,
            onDateSelected = { date ->
                locationViewModel.updateAssessmentDate(date)
            }
        )

        // assessedBy
        DropdownWithLabel(
            label = "Assess By: ",
            suggestions = projects.map { it.assessedBy }.distinct(),
            selectedText = selectedAssessmentPerson,
            onTextSelected = { selectedPerson ->
                selectedAssessmentPerson = selectedPerson
            }
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        )

        // save assessment
        Button(onClick = {
            try {
                val savePhotoOne = saveFileToDownloads(context, photoOneUri!!)
                val savePhotoTwo = saveFileToDownloads(context, photoTwoUri!!)
                val savePhotoThree = saveFileToDownloads(context, photoThreeUri!!)
                val savePhotoFour = saveFileToDownloads(context, photoFourUri!!)

                val locationEntity = LocationEntity(
                    id = 0,
                    groupName = selectedProjectDetails!!.groupName,
                    groupDescription = selectedProjectDetails!!.groupDescription,
                    foundingDate = selectedProjectDetails!!.foundingDate,
                    registered = selectedProjectDetails!!.registered,
                    registrationNumber = selectedProjectDetails!!.registrationNumber,
                    registrationDate = selectedProjectDetails!!.registrationDate,
                    village = selectedProjectDetails!!.village,
                    parish = selectedProjectDetails!!.parish,
                    subCounty = selectedProjectDetails!!.subCounty,
                    county = selectedProjectDetails!!.county,
                    district = selectedProjectDetails!!.district,
                    subRegion = selectedProjectDetails!!.subRegion,
                    country = selectedProjectDetails!!.country,
                    createdBy = selectedProjectDetails!!.createdBy,
                    createdOn = selectedProjectDetails!!.createdOn,
                    uuid = selectedProjectDetails!!.uuid,
                    memberShipNumber = selectedProjectDetails!!.memberShipNumber,
                    firstName = selectedProjectDetails!!.firstName,
                    lastName = selectedProjectDetails!!.lastName,
                    otherName = selectedProjectDetails!!.otherName,
                    gender = selectedProjectDetails!!.gender,
                    dob = selectedProjectDetails!!.dob,
                    paid = selectedProjectDetails!!.paid,
                    memberRole = selectedProjectDetails!!.memberRole,
                    memberPhotoPath = selectedProjectDetails!!.memberPhotoPath,
                    otherDetails = selectedProjectDetails!!.otherDetails,
                    projectNumber = selectedProjectDetails!!.projectNumber,
                    projectName = selectedProjectDetails!!.projectName,
                    projectFocus = selectedProjectDetails!!.projectFocus,
                    startDate = Dates.startDate,
                    endDate = Dates.endDate,
                    expectedDate = Dates.expectedEndDate,
                    fundedBy = selectedProjectDetails!!.fundedBy,
                    amount = selectedProjectDetails!!.amount,
                    teamLeader = selectedProjectDetails!!.teamLeader,
                    teamLeaderEmail = selectedProjectDetails!!.teamLeaderEmail,
                    teamLeaderPhone = selectedProjectDetails!!.teamLeaderPhone,
                    otherProjectContacts = selectedProjectDetails!!.otherProjectContacts,
                    projectDescription = selectedProjectDetails!!.projectDescription,
                    assessmentDate = Dates.assessmentDate,
                    assessedBy = selectedAssessmentPerson,
                    assessMilestone = assessMilestones,
                    assessmentFor = assessmentFor,
                    observation = obs,
                    photoOnePath = savePhotoOne,
                    photoTwoPath = savePhotoTwo,
                    photoThreePath = savePhotoThree,
                    photoFourPath = savePhotoFour,
                    latitude = userLocation.latitude,
                    longitude = userLocation.longitude,
                    altitude = selectedProjectDetails!!.altitude,
                    gpsCrs = selectedProjectDetails!!.gpsCrs,
                    milestoneDate = Dates.milestoneAssessmentDate,
                    milestoneDetails = selectedProjectDetails!!.milestoneDetails,
                    milestoneTarget = selectedProjectDetails!!.milestoneTarget,
                    milestoneTargetDate = selectedProjectDetails!!.milestoneTargetDate,
                    assignedTo = selectedProjectDetails!!.assignedTo,
                    status = selectedProjectDetails!!.status,
                    mileStoneComments = selectedProjectDetails!!.mileStoneComments,
                    milestonePhotoOnePath = selectedProjectDetails!!.milestonePhotoOnePath,
                    milestonePhotoTwoPath = selectedProjectDetails!!.milestonePhotoTwoPath,
                    milestonePhotoThreePath = selectedProjectDetails!!.milestonePhotoThreePath,
                    milestonePhotoFourPath = selectedProjectDetails!!.milestonePhotoFourPath
                )

                locationViewModel.saveLocation(locationEntity)
                Toast.makeText(context, "Data saved successfully!!", Toast.LENGTH_SHORT).show()

                // step 4
                locationViewModel.updateAssessedBy("")
                locationViewModel.updateAssessMilestone(false)
                locationViewModel.updateAssessmentFor("")
                locationViewModel.updateObs("")
                locationViewModel.updatePhotoOneUri(null)
                locationViewModel.updatePhotoTwoUri(null)
                locationViewModel.updatePhotoThreeUri(null)
                locationViewModel.updatePhotoFourUri(null)
                locationViewModel.updateAltitude("")
                locationViewModel.updateGps("")

                // empty all dates too
                Dates.foundingDates = ""
                Dates.registrationDate = ""
                Dates.creationDate = ""
                Dates.dob = ""
                Dates.startDate = ""
                Dates.endDate = ""
                Dates.expectedEndDate = ""
                Dates.assessmentDate = ""
                Dates.milestoneAssessmentDate = ""
                Dates.targetDate = ""

                navController.navigate("SavedImages")

            } catch(e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                Log.e("Saving assessment failed", "saving data failed due to ${e.message}")
                throw RuntimeException("Capture: ${e.message}")
            }

        }, modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp, bottom = 40.dp)
        ) {
            Text("Save Assessment")
        }


    }

}