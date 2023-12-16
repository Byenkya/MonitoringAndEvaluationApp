package com.example.monitoringandevaluationapp.presentation.CaptureData

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.example.monitoringandevaluationapp.R
import com.example.monitoringandevaluationapp.data.Dates
import com.example.monitoringandevaluationapp.usecases.LocationViewModel
import java.util.Objects
import java.util.UUID

@Composable
fun StepTwo(locationViewModel: LocationViewModel) {
    val pattern = remember { Regex("^\\d+\$") }
    val memberUuid = remember { UUID.randomUUID().toString() }
    val memberShipNumber by locationViewModel.memberShipNumber.collectAsState()
    val firstName by locationViewModel.firstName.collectAsState()
    val lastName by locationViewModel.lastName.collectAsState()
    val otherName by locationViewModel.otherName.collectAsState()
    val selectedGender by locationViewModel.gender.collectAsState()
    val dob = remember { mutableStateOf("") }
    val paidSubscription by locationViewModel.paidSubscription.collectAsState()
    val memberRole by locationViewModel.memberRole.collectAsState()
    val memberImageUri by locationViewModel.memberImageUri.collectAsState()
    val otherDetails by locationViewModel.otherDetails.collectAsState()
    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        "${context.packageName}.provider",
        file
    )
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        locationViewModel.updateMemberImageUri(uri)
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
        Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
    ) {
        Text("Membership Information", fontSize = 18.sp)
        Divider(color = Color.Black, thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))

        // MemberUuid
        Text(
            text = "Uuid: $memberUuid", fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )
        locationViewModel.updateMemberUuid(memberUuid)

        // Membership number
        TextField(
            value = memberShipNumber.toString(),
            onValueChange = {
                if (it.matches(pattern)) {
                    locationViewModel.updateMemberShipNumber(it.toInt())
                }
            },
            label = { Text("Enter MemberShip Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )


        // First Name
        TextField(
            value = firstName,
            onValueChange = { locationViewModel.updateFirstName(it) },
            label = { Text("First Name") },
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )

        // Last Name
        TextField(
            value = lastName,
            onValueChange = { locationViewModel.updateLastName(it) },
            label = { Text("Last Name") },
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )

        // Other Name
        TextField(
            value = otherName,
            onValueChange = { locationViewModel.updateOtherName(it) },
            label = { Text("Other Name") },
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )

        // Gender
        DropdownWithLabel(
            label = "Gender",
            suggestions = listOf("Male", "Female"),
            selectedText = selectedGender,
            onTextSelected = { gender ->
                locationViewModel.updateGender(gender)
            }
        )

        // DOB
        DateFieldWithPicker(
            label = "Date of Birth",
            dates = Dates,
            date = dob,
            onDateSelected = { date ->
                locationViewModel.updateDOB(date)
            }
        )

        // paid Subscription
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Paid Subscription")
            Spacer(modifier = Modifier.width(4.dp))
            Checkbox(
                checked = paidSubscription,
                onCheckedChange = { checked ->
                    locationViewModel.updatePaidSubscription(checked)
                }
            )
        }

        // Member role
        TextField(
            value = memberRole,
            onValueChange = { locationViewModel.updateMemberRole(it) },
            label = { Text("Member Role") },
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )

        // Member Photo
        if (memberImageUri?.path?.isNotEmpty() == true) {
            Image(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .border(2.dp, Color.Gray),
                painter = rememberAsyncImagePainter(memberImageUri),
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
                            "Camera being used by another program",
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
            Text("Capture Member Photo")
        }

        // Other details
        TextField(
            value = otherDetails,
            onValueChange = { locationViewModel.updateOtherDetails(it) },
            label = { Text("Other Details") },
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )


    }

}

