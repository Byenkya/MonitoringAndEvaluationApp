package com.example.monitoringandevaluationapp.presentation.CaptureData

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.example.monitoringandevaluationapp.data.Dates
import com.example.monitoringandevaluationapp.usecases.LocationViewModel
import java.util.Objects

@Composable
fun StepFive(locationViewModel: LocationViewModel) {
    val milestoneAssessmentDate = remember { mutableStateOf("") }
    val milestoneDetails by locationViewModel.milestoneDetails.collectAsState()
    val milestoneTarget by locationViewModel.mileStoneTarget.collectAsState()
    val milestoneTargetDate = remember { mutableStateOf("") }
    val assignedTo by locationViewModel.assignedTo.collectAsState()
    val milestoneStatus by locationViewModel.milestoneStatus.collectAsState()
    val milestoneComments by locationViewModel.milestoneComments.collectAsState()
    val milestonePhotoOneUri by locationViewModel.milestonePhotoOneUri.collectAsState()
    val milestonePhotoTwoUri by locationViewModel.milestonePhotoTwoUri.collectAsState()
    val milestonePhotoThreeUri by locationViewModel.milestonePhotoThreeUri.collectAsState()
    val milestonePhotoFourUri by locationViewModel.milestonePhotoFourUri.collectAsState()
    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        "${context.packageName}.provider",
        file
    )
    val cameraLauncher1 = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        locationViewModel.updateMilestonePhotoOneUri(uri)
    }

    val cameraLauncher2 = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        locationViewModel.updateMilestonePhotoTwoUri(uri)
    }

    val cameraLauncher3 = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        locationViewModel.updateMilestonePhotoThreeUri(uri)
    }

    val cameraLauncher4 = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        locationViewModel.updateMilestonePhotoFourUri(uri)
    }

    Column(
        Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
    ) {
        Text("Milestones", fontSize = 18.sp)
        Divider(color = Color.Black, thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))

        // milestone assessment date
        DateFieldWithPicker(
            label = "Date",
            dates = Dates,
            date = milestoneAssessmentDate,
            onDateSelected = { date ->
                locationViewModel.updateMileStoneAssessmentDate(date)
            }
        )

        // milestone details
        TextField(
            value = milestoneDetails,
            onValueChange = { locationViewModel.updateMilestoneDetails(it) },
            label = { Text("Milestone Details") },
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )

        // milestone target
        TextField(
            value = milestoneTarget,
            onValueChange = { locationViewModel.updateMileStoneTarget(it) },
            label = { Text("Milestone Target") },
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )

        // milestone target date
        DateFieldWithPicker(
            label = "Target Date",
            dates = Dates,
            date = milestoneTargetDate,
            onDateSelected = { date ->
                locationViewModel.updateMileStoneTargetDate(date)
            }
        )

        // AssignedTo
        TextField(
            value = assignedTo,
            onValueChange = { locationViewModel.updateAssignedTo(it) },
            label = { Text("Assigned To") },
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )

        // milestone status
        AssessmentForRadioGroupMileStone(
            selectedAssessmentFor = milestoneStatus,
            onAssessmentForSelected = { assessmentFor -> locationViewModel.updateMilestoneStatus(assessmentFor) }
        )

        // comments
        TextField(
            value = milestoneComments,
            onValueChange = { locationViewModel.updateMilestoneComments(it) },
            label = { Text("Milestone Comments") },
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )

        //milestone photo 1
        ImageCaptureButton(
            title = "Milestone Photo 1",
            context = context,
            cameraLauncher = cameraLauncher1,
            uri = uri,
            locationViewModel = locationViewModel,
            imageUri = milestonePhotoOneUri ?: Uri.EMPTY,
            imageUriUpdater = { updatedUri -> locationViewModel.updateMilestonePhotoOneUri(updatedUri) }
        )

        //milestone photo 2
        ImageCaptureButton(
            title = "Milestone Photo 2",
            context = context,
            cameraLauncher = cameraLauncher2,
            uri = uri,
            locationViewModel = locationViewModel,
            imageUri = milestonePhotoTwoUri ?: Uri.EMPTY,
            imageUriUpdater = { updatedUri -> locationViewModel.updateMilestonePhotoTwoUri(updatedUri) }
        )

        //milestone photo 3
        ImageCaptureButton(
            title = "Milestone Photo 3",
            context = context,
            cameraLauncher = cameraLauncher3,
            uri = uri,
            locationViewModel = locationViewModel,
            imageUri = milestonePhotoThreeUri ?: Uri.EMPTY,
            imageUriUpdater = { updatedUri -> locationViewModel.updateMilestonePhotoThreeUri(updatedUri) }
        )

        //milestone photo 4
        ImageCaptureButton(
            title = "Milestone Photo 4",
            context = context,
            cameraLauncher = cameraLauncher4,
            uri = uri,
            locationViewModel = locationViewModel,
            imageUri = milestonePhotoFourUri ?: Uri.EMPTY,
            imageUriUpdater = { updatedUri -> locationViewModel.updateMilestonePhotoFourUri(updatedUri) }
        )

    }

}

@Composable
fun AssessmentForRadioGroupMileStone(
    selectedAssessmentFor: String,
    onAssessmentForSelected: (String) -> Unit
) {
    val options = listOf("Complete", "InComplete", "Pending")

    Column {
        Text("Select Status For", modifier = Modifier.padding(16.dp))

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