package com.example.monitoringandevaluationapp.presentation.ui.ProjectAssessment

import android.Manifest
import android.app.Activity
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.example.monitoringandevaluationapp.data.Dates
import com.example.monitoringandevaluationapp.presentation.ui.CaptureData.AssessmentForRadioGroup
import com.example.monitoringandevaluationapp.presentation.ui.CaptureData.DateFieldWithPicker
import com.example.monitoringandevaluationapp.presentation.ui.CaptureData.DropdownWithLabel
import com.example.monitoringandevaluationapp.presentation.ui.CaptureData.ImageCaptureButton
import com.example.monitoringandevaluationapp.presentation.ui.CaptureData.createImageFile
import com.example.monitoringandevaluationapp.presentation.ViewModel.LocationViewModel
import com.google.android.gms.maps.model.LatLng
import java.util.Objects
import androidx.lifecycle.Observer
import coil.compose.rememberAsyncImagePainter
import com.example.monitoringandevaluationapp.R
import com.example.monitoringandevaluationapp.data.GeometryUtils
import com.example.monitoringandevaluationapp.data.GroupEntity
import com.example.monitoringandevaluationapp.data.PdmProjectAssessmentEntity
import com.example.monitoringandevaluationapp.data.PdmProjectEntity
import com.example.monitoringandevaluationapp.data.UserLocation
import com.example.monitoringandevaluationapp.data.repository.PdmProjectAssessmentRepository
import com.example.monitoringandevaluationapp.presentation.ViewModel.GroupViewModel
import com.example.monitoringandevaluationapp.presentation.ViewModel.PdmProjectLocalViewModel
import com.example.monitoringandevaluationapp.presentation.ui.CaptureData.MissingFieldsDialog
import com.example.monitoringandevaluationapp.presentation.ui.CaptureData.captureImage
import com.example.monitoringandevaluationapp.presentation.ui.CaptureData.saveFileToDownloads
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID
import androidx.compose.runtime.LaunchedEffect as LaunchedEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectAssessment(
    navController: NavController,
    projectPdmProjectLocationViewModel: PdmProjectLocalViewModel,
    groupViewModel: GroupViewModel,
    pdmProjectAssessmentRepository: PdmProjectAssessmentRepository
) {
    val scope = rememberCoroutineScope()
    val missingFields = mutableListOf<String>()
    val errorMessage = remember { mutableStateOf("") }
    val showDialog = remember { mutableStateOf(false) }
    val lifecycleOwner = LocalLifecycleOwner.current
    var selectedProject by remember { mutableStateOf("") }
    var selectedAssessmentPerson by remember { mutableStateOf("") }
    val groups = remember { mutableStateListOf<GroupEntity>() }
    var groupName by remember { mutableStateOf("") }
    var selectedGroup by remember { mutableStateOf("") }
    var groupId by remember { mutableStateOf("") }
    var projID by remember { mutableStateOf(0L) }
    var projName by remember { mutableStateOf("") }
    val projects = remember { mutableStateListOf<PdmProjectEntity>() }
    var selectedProjectDetails by remember { mutableStateOf<PdmProjectEntity?>(null) }
    val assessmentDate = remember { mutableStateOf("") }
    var assessMilestones by remember { mutableStateOf("") }
    var assessmentFor by remember { mutableStateOf("") }
    var obs by remember { mutableStateOf("") }
    val assessedPhotoOneUri by projectPdmProjectLocationViewModel.assessedPhotoOneUri.collectAsState()
    val assessedPhotoTwoUri by projectPdmProjectLocationViewModel.assessedPhotoTwoUri.collectAsState()
    val assessedPhotoThreeUri by projectPdmProjectLocationViewModel.assessedPhotoThreeUri.collectAsState()
    val assessedPhotoFourUri by projectPdmProjectLocationViewModel.assessedPhotoFourUri.collectAsState()
    val milestonePhotoOneUri by projectPdmProjectLocationViewModel.milestonePhotoOneUri.collectAsState()
    val milestonePhotoTwoUri by projectPdmProjectLocationViewModel.milestonePhotoTwoUri.collectAsState()
    val milestonePhotoThreeUri by projectPdmProjectLocationViewModel.milestonePhotoThreeUri.collectAsState()
    val milestonePhotoFourUri by projectPdmProjectLocationViewModel.milestonePhotoFourUri.collectAsState()
    var milestoneDetails by remember { mutableStateOf("") }
    var milestoneTarget by remember { mutableStateOf("") }
    var milestoneTargetDate = remember { mutableStateOf("") }
    var assignedTo by remember { mutableStateOf("") }
    var milestoneStatus by remember { mutableStateOf("") }
    var userLocation by remember{ mutableStateOf(LatLng(0.0, 0.0)) }
    val uuid = remember { UUID.randomUUID().toString() }

    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        "${context.packageName}.provider",
        file
    )

    // Declare request code for location permission
    val WRITE_REQUEST_CODE = 1003
    val READ_REQUEST_CODE = 1004
    val CAMERA_REQUEST_CODE = 1005

    val hasWritePermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    if (!hasWritePermission) {
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

    if (!hasReadPermission) {
        // Request read permission
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            READ_REQUEST_CODE
        )
    }

    val hasCameraPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    if (!hasCameraPermission) {
        // Request camera permission
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE
        )
    }

    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE
        )
    }

    val cameraLauncher1 = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        projectPdmProjectLocationViewModel.updateAssessedPhotoOneUri(uri)
    }

    val cameraLauncher2 = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        projectPdmProjectLocationViewModel.updateAssessedPhotoTwoUri(uri)
    }

    val cameraLauncher3 = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        projectPdmProjectLocationViewModel.updateAssessedPhotoThreeUri(uri)
    }

    val cameraLauncher4 = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        projectPdmProjectLocationViewModel.updateAssessedPhotoFourUri(uri)
    }

    val cameraLauncher5 = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        projectPdmProjectLocationViewModel.updateMilestonePhotoOneUri(uri)
    }

    val cameraLauncher6 = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        projectPdmProjectLocationViewModel.updateMilestonePhotoTwoUri(uri)
    }

    val cameraLauncher7 = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        projectPdmProjectLocationViewModel.updateMilestonePhotoThreeUri(uri)
    }

    val cameraLauncher8 = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        projectPdmProjectLocationViewModel.updateMilestonePhotoFourUri(uri)
    }


    LaunchedEffect(key1 = projectPdmProjectLocationViewModel.allPdmLocalProjects) {
        projectPdmProjectLocationViewModel.allPdmLocalProjects.observeForever { newList ->
            projects.clear()
            projects.addAll(newList)
        }
    }

    LaunchedEffect(key1 = groupViewModel.allgroups) {
        groupViewModel.allgroups.observeForever { newList ->
            groups.clear()
            groups.addAll(newList)
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


        DropdownWithLabel(
            label = "Select Project",
            suggestions = projects?.map { it.projName }?.distinct() ?: emptyList(),
            selectedText = selectedProject,
            onTextSelected = { projectName ->
                selectedProject = projectName
                selectedProjectDetails = projects?.firstOrNull { it.projName == projectName }

                selectedProjectDetails?.let { project ->
                    projID = project.id
                    projName = project.projName
                }

                if (selectedProjectDetails == null) {
                    // Handle the case when the selected project is not found in the list
                    // For example, you can display an error message or perform some other action
                    Log.e("DropdownError", "Selected project not found: $projectName")
                }
            }
        )


        // select group
        DropdownWithLabel(
            label = "Select Group",
            suggestions = groups.map { it.name }.distinct(),
            selectedText = selectedGroup,
            onTextSelected = { selectedName ->
                val selectedProjectGroup = groups.firstOrNull { it.name == selectedName }
                if (selectedProjectGroup != null) {
                    selectedGroup = selectedName
                    groupName = selectedName
                    groupId = selectedProjectGroup.id.toString()
                }
            }
        )

        // assessedBy
        DropdownWithLabelAssessedBy(
            label = "Select Assessor: ",
            suggestions = projects.map { it.teamLeader }.distinct(),
            selectedText = selectedAssessmentPerson,
            onTextSelected = { selectedPerson ->
                selectedAssessmentPerson = selectedPerson
            }
        )

        // Assessment Date
        DateFieldWithPicker(
            label = "Assessment Date",
            dates = Dates,
            date = assessmentDate,
            onDateSelected = {}
        )

        // assess milestone
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Text(text = "Assess Milestone")
//            Spacer(modifier = Modifier.width(4.dp))
//            Checkbox(
//                checked = assessMilestones,
//                onCheckedChange = { checked ->
//                    locationViewModel.updateAssessMilestone(checked)
//                }
//            )
//        }

        // assessment for
        AssessmentForRadioGroup(
            selectedAssessmentFor = assessmentFor,
            onAssessmentForSelected = { assessmentFor = it }
        )

        // observation
        TextField(
            value = obs,
            onValueChange = { obs = it },
            label = { Text("Observation") },
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )

        //photo 1
        ImageCaptureButton1(
            title = "Assessed Photo 1",
            context = context,
            cameraLauncher = cameraLauncher1,
            uri = uri,
            projectPdmProjectLocationViewModel = projectPdmProjectLocationViewModel,
            imageUri = assessedPhotoOneUri,
            imageUriUpdater = { updatedUri ->
                projectPdmProjectLocationViewModel.updateAssessedPhotoOneUri(updatedUri)
            }
        )

        // photo 2
        ImageCaptureButton1(
            title = "Assessed Photo 2",
            context = context,
            cameraLauncher = cameraLauncher2,
            uri = uri,
            projectPdmProjectLocationViewModel = projectPdmProjectLocationViewModel,
            imageUri = assessedPhotoTwoUri,
            imageUriUpdater = { updatedUri ->
                projectPdmProjectLocationViewModel.updateAssessedPhotoTwoUri(updatedUri)
            }
        )

        // photo 3
        ImageCaptureButton1(
            title = "Assessed Photo 3",
            context = context,
            cameraLauncher = cameraLauncher3,
            uri = uri,
            projectPdmProjectLocationViewModel = projectPdmProjectLocationViewModel,
            imageUri = assessedPhotoThreeUri,
            imageUriUpdater = { updatedUri ->
                projectPdmProjectLocationViewModel.updateAssessedPhotoThreeUri(updatedUri)
            }
        )

        // photo 4
        ImageCaptureButton1(
            title = "Assessed Photo 4",
            context = context,
            cameraLauncher = cameraLauncher4,
            uri = uri,
            projectPdmProjectLocationViewModel = projectPdmProjectLocationViewModel,
            imageUri = assessedPhotoFourUri ?: Uri.EMPTY,
            imageUriUpdater = { updatedUri ->
                projectPdmProjectLocationViewModel.updateAssessedPhotoFourUri(updatedUri)
            }
        )

        // milestone details
        TextField(
            value = milestoneDetails,
            onValueChange = { milestoneDetails = it },
            label = { Text("Milestone Details") },
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )

        // milestone targets
        TextField(
            value = milestoneTarget,
            onValueChange = { milestoneTarget = it },
            label = { Text("Milestone Targets") },
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )

        // milestone target date
        DateFieldWithPicker(
            label = "Target Date",
            dates = Dates,
            date = milestoneTargetDate,
            onDateSelected = {}
        )

        // AssignedTo
        TextField(
            value = assignedTo,
            onValueChange = { assignedTo = it },
            label = { Text("Assigned To") },
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )

        // milestoneStatus
        TextField(
            value = milestoneStatus,
            onValueChange = { milestoneStatus = it },
            label = { Text("Milestone Status") },
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )

        // milestone photo
        ImageCaptureButton1(
            title = "Milestone Photo 1",
            context = context,
            cameraLauncher = cameraLauncher5,
            uri = uri,
            projectPdmProjectLocationViewModel = projectPdmProjectLocationViewModel,
            imageUri = milestonePhotoOneUri,
            imageUriUpdater = { updatedUri ->
                projectPdmProjectLocationViewModel.updateMilestonePhotoOneUri(updatedUri)
            }
        )

        // milestone photo 2
        ImageCaptureButton1(
            title = "Milestone Photo 2",
            context = context,
            cameraLauncher = cameraLauncher6,
            uri = uri,
            projectPdmProjectLocationViewModel = projectPdmProjectLocationViewModel,
            imageUri = milestonePhotoTwoUri,
            imageUriUpdater = { updatedUri ->
                projectPdmProjectLocationViewModel.updateMilestonePhotoTwoUri(updatedUri)
            }
        )

        // milestone photo 3
        ImageCaptureButton1(
            title = "Milestone Photo 3",
            context = context,
            cameraLauncher = cameraLauncher7,
            uri = uri,
            projectPdmProjectLocationViewModel = projectPdmProjectLocationViewModel,
            imageUri = milestonePhotoThreeUri,
            imageUriUpdater = { updatedUri ->
                projectPdmProjectLocationViewModel.updateMilestonePhotoThreeUri(updatedUri)
            }
        )

        // milestone photo 4
        ImageCaptureButton1(
            title = "Milestone Photo 4",
            context = context,
            cameraLauncher = cameraLauncher8,
            uri = uri,
            projectPdmProjectLocationViewModel = projectPdmProjectLocationViewModel,
            imageUri = milestonePhotoFourUri ?: Uri.EMPTY,
            imageUriUpdater = { updatedUri ->
                projectPdmProjectLocationViewModel.updateMilestonePhotoFourUri(updatedUri)
            }
        )

        // Divider
        Divider(color = Color.Black, thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))

        // save assessment
        Button(onClick = {
            try {
                if(Dates.assessmentDate.isNullOrBlank()) {
                    missingFields.add("Assessment Date Missing")
                }

                if (selectedAssessmentPerson.isNullOrBlank()) {
                    missingFields.add("Assessment By Missing")
                }

                if(assessmentFor.isNullOrBlank()) {
                    missingFields.add("Assessment For Missing")
                }

                if(obs.isNullOrBlank()) {
                    missingFields.add("Observation Missing")
                }

                if(assessedPhotoOneUri == null) {
                    missingFields.add("Photo One Missing Missing")
                }

                if(assessedPhotoTwoUri == null) {
                    missingFields.add("Photo Two Missing Missing")
                }

                if(assessedPhotoThreeUri == null) {
                    missingFields.add("Photo Three Missing")
                }

                if(assessedPhotoFourUri == null) {
                    missingFields.add("Photo Four Missing")
                }

                if(milestonePhotoOneUri == null) {
                    missingFields.add("Milestone photo one Missing")
                }

                if(milestonePhotoTwoUri == null) {
                    missingFields.add("Milestone Photo Two Missing")
                }

                if(milestonePhotoThreeUri == null) {
                    missingFields.add("Milestone Photo Three Missing")
                }

                if(milestonePhotoFourUri == null) {
                    missingFields.add("Milestone Photo Four Missing")
                }

                if (missingFields.isEmpty()) {
                    // Get the current date and time
                    val currentDate = Date()

                    // Define a date format
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

                    // Format the current date and time into a string
                    val formattedDate = dateFormat.format(currentDate)

                    val savePhotoOne = saveFileToDownloads(context, assessedPhotoOneUri!!)
                    val savePhotoTwo = saveFileToDownloads(context, assessedPhotoTwoUri!!)
                    val savePhotoThree = saveFileToDownloads(context, assessedPhotoThreeUri!!)
                    val savePhotoFour = saveFileToDownloads(context, assessedPhotoFourUri!!)
                    val savePhotoFive = saveFileToDownloads(context, milestonePhotoOneUri!!)
                    val savePhotoSix = saveFileToDownloads(context, milestonePhotoTwoUri!!)
                    val savePhotoSeven = saveFileToDownloads(context, milestonePhotoThreeUri!!)
                    val savePhotoEight = saveFileToDownloads(context, milestonePhotoFourUri!!)

                    val geom = GeometryUtils.byteArrayToHexString(
                        GeometryUtils.createGeomFromLatLng(
                            UserLocation.lat,
                            UserLocation.long
                        )
                    )

                    val pdmProjectAssessment = PdmProjectAssessmentEntity(
                        id = 0,
                        geom = geom,
                        uuid = uuid,
                        groupName = groupName,
                        groupId = groupId.toLong(),
                        lat = UserLocation.long,
                        longitude = UserLocation.lat,
                        assessedBy = selectedAssessmentPerson,
                        dateAssessed = Dates.assessmentDate,
                        assessMilestones = assessMilestones,
                        assessedFor = assessmentFor,
                        observation = obs,
                        assessedPhotoOnePath = savePhotoOne,
                        assessedPhotoTwoPath = savePhotoTwo,
                        assessedPhotoThreePath = savePhotoThree,
                        assessedPhotoFourPath = savePhotoFour,
                        milestoneDetails = milestoneDetails,
                        milestoneTarget = milestoneTarget,
                        milestoneTargetDate = Dates.targetDate,
                        assignedTo = assignedTo,
                        milestoneStatus = milestoneStatus,
                        milestonePhotoOnePath = savePhotoFive,
                        milestonePhotoTwoPath = savePhotoSix,
                        milestonePhotoThreePath = savePhotoSeven,
                        milestonePhotoFourPath = savePhotoEight,
                        updatedBy = formattedDate,
                        dateUpdated = formattedDate,
                        projectName = projName,
                        projectID = projID,
                        uploaded = false
                    )
                    scope.launch {
                        pdmProjectAssessmentRepository.saveProjectAssessment(pdmProjectAssessment)
                        Toast.makeText(context, "Data saved successfully!!", Toast.LENGTH_SHORT).show()
                        projectPdmProjectLocationViewModel.updateAssessedPhotoOneUri(null)
                        projectPdmProjectLocationViewModel.updateAssessedPhotoTwoUri(null)
                        projectPdmProjectLocationViewModel.updateAssessedPhotoThreeUri(null)
                        projectPdmProjectLocationViewModel.updateAssessedPhotoFourUri(null)
                        projectPdmProjectLocationViewModel.updateMilestonePhotoOneUri(null)
                        projectPdmProjectLocationViewModel.updateMilestonePhotoTwoUri(null)
                        projectPdmProjectLocationViewModel.updateMilestonePhotoThreeUri(null)
                        projectPdmProjectLocationViewModel.updateMilestonePhotoFourUri(null)
                        navController.navigate("SavedProjects")
                    }
                } else {
                    showDialog.value = true
                    val missingFieldsMessage = "Please provide information for the following fields:\n${missingFields.joinToString(",\n")}"
                    errorMessage.value = missingFieldsMessage
                }

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

        if (showDialog.value) {
            MissingFieldsDialog(mainDialog = showDialog, message = errorMessage, onDismiss = { /* handle dismiss */ })
        }


    }

}
@Composable
fun DropdownWithLabelAssessedBy(
    label: String,
    suggestions: List<String>,
    selectedText: String,
    onTextSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    var searchQuery by remember { mutableStateOf("") }
    var customAssessor by remember { mutableStateOf("") }

    val icon = Icons.Filled.ArrowDropDown

    Box {
        OutlinedTextField(
            value = selectedText,
            onValueChange = { /* No-op, as it's readOnly */ },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                },
            label = { Text(label) },
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = "DropDown",
                    modifier = Modifier.clickable { expanded = !expanded }
                )
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
        ) {
            // Add a search TextField at the top
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                    },
                    placeholder = { Text("Search") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Display filtered suggestions
//            / Filter suggestions based on the search query
            val filteredSuggestions = suggestions.filter {
                it.contains(searchQuery, ignoreCase = true)
            }

            // Display filtered suggestions
            filteredSuggestions.forEach { suggestion ->
                androidx.compose.material.DropdownMenuItem(onClick = {
                    onTextSelected(suggestion)
                    expanded = false
                }) {
                    Text(text = suggestion)
                }
            }

            // Divider
            Divider(color = Color.Black, thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))

            // Allow user to add a custom assessor
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                TextField(
                    value = customAssessor,
                    onValueChange = {
                        customAssessor = it
                    },
                    placeholder = { Text("Add Project assessor") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Add button to confirm custom assessor
            androidx.compose.material.DropdownMenuItem(onClick = {
                if (customAssessor.isNotBlank()) {
                    onTextSelected(customAssessor)
                    expanded = false
                }
            }) {
                Text(text = "Add Project Assessor", fontWeight = FontWeight.Bold)
            }

        }
    }
}

fun captureImage1(
    context: Context,
    cameraLauncher: ActivityResultLauncher<Uri>,
    uri: Uri,
    projectPdmProjectLocationViewModel: PdmProjectLocalViewModel,
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
                    "Camera not available",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    } catch (e: Exception) {
        Log.e("Launch Camera", "Starting camera failed due to ${e.message}")
    }
}


@Composable
fun ImageCaptureButton1(
    title: String,
    context: Context,
    cameraLauncher: ActivityResultLauncher<Uri>,
    uri: Uri,
    projectPdmProjectLocationViewModel: PdmProjectLocalViewModel,
    imageUri: Uri?,
    modifier: Modifier = Modifier,
    imageUriUpdater: (Uri) -> Unit
) {
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            captureImage1(context, cameraLauncher, uri, projectPdmProjectLocationViewModel, imageUriUpdater)
        } else {
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
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
            captureImage1(context, cameraLauncher, uri, projectPdmProjectLocationViewModel, imageUriUpdater)
        },
        modifier = modifier.fillMaxWidth()
    ) {
        Text("Capture $title")
    }
}

