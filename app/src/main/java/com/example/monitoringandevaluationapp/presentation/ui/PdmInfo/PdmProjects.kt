package com.example.monitoringandevaluationapp.presentation.ui.PdmInfo

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField

import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.monitoringandevaluationapp.data.Dates
import com.example.monitoringandevaluationapp.data.GroupEntity
import com.example.monitoringandevaluationapp.data.PdmProjectEntity
import com.example.monitoringandevaluationapp.data.UserLocation
import com.example.monitoringandevaluationapp.data.api.model.ApiResponse
import com.example.monitoringandevaluationapp.presentation.ViewModel.GroupViewModel
import com.example.monitoringandevaluationapp.presentation.ViewModel.PdmProjectLocalViewModel
import com.example.monitoringandevaluationapp.presentation.ui.CaptureData.DateFieldWithPicker
import com.example.monitoringandevaluationapp.presentation.ui.CaptureData.DropdownWithLabel
import com.example.monitoringandevaluationapp.presentation.ui.CaptureData.MissingFieldsDialog
import com.example.monitoringandevaluationapp.presentation.ui.ProjectAssessments.LoadingDialog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.Modifier

import androidx.compose.runtime.*
import com.example.monitoringandevaluationapp.data.GeometryUtils


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdmProjectTabViewContent(
    navController: NavController,
    groupViewModel: GroupViewModel,
    projectLocalViewModel: PdmProjectLocalViewModel
) {
    val pattern = remember { Regex("^\\d+\$") }
    val emailRegex = Regex("^\\S+@\\S+\\.\\S+$")
    var isEmailValid by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val groups = remember { mutableStateListOf<GroupEntity>() }
    var selectedGroup by remember { mutableStateOf("") }
    var isUploading by remember { mutableStateOf(false) }
    var apiResponse = ApiResponse("")
    val scope = rememberCoroutineScope()
    val missingFields = mutableListOf<String>()
    val errorMessage = remember { mutableStateOf("") }
    val showDialog = remember { mutableStateOf(false) }
    val uuid =  remember { UUID.randomUUID().toString() }
    var groupName by remember { mutableStateOf("") }
    var groupId by remember { mutableStateOf("") }
    var projNo by remember { mutableStateOf("") }
    var projName by remember { mutableStateOf("") }
    var projFocus by remember { mutableStateOf("") }
    var projDesc by remember { mutableStateOf("") }
    val startDate = remember { mutableStateOf("") }
    val endDate = remember { mutableStateOf("") }
    var fundedBy by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf(0L) }
    var teamLeader by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telephone by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    var createdBy by remember { mutableStateOf("") }
    var dateCreated by remember { mutableStateOf("") }
    var updatedBy by remember { mutableStateOf("") }
    var dateUpdated by remember { mutableStateOf("") }
    var latX by remember { mutableStateOf("") }
    var lonY by remember { mutableStateOf("") }

    LaunchedEffect(key1 = groupViewModel.allgroups) {
        groupViewModel.allgroups.observeForever { newList ->
            groups.clear()
            groups.addAll(newList)
        }
    }

    Column(
        modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState())
    ) {
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

        // projNo
        TextField(
            value = projNo,
            onValueChange = { projNo = it },
            label = { Text("Project Number") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        // projName
        TextField(
            value = projName,
            onValueChange = { projName = it },
            label = { Text("Project Name") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        // projFocus
        TextField(
            value = projFocus,
            onValueChange = { projFocus = it },
            label = { Text("Project Focus") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        // projectDesc
        TextField(
            value = projDesc,
            onValueChange = { projDesc = it },
            label = { Text("Project Description") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        // startDate
        DateFieldWithPicker(
            label = "Pdm Start Date",
            dates = Dates,
            date = startDate,
            onDateSelected = {}
        )

        // EndDate
        DateFieldWithPicker(
            label = "Pdm End Date",
            dates = Dates,
            date = endDate,
            onDateSelected = {}
        )

        // FundedBy
        androidx.compose.material3.TextField(
            value = fundedBy,
            onValueChange = { fundedBy = it },
            label = { androidx.compose.material3.Text("Funded By") },
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )

        // Amount
        androidx.compose.material3.TextField(
            value = amount.toString(),
            onValueChange = { newString ->
                if (newString.matches(pattern)) {
                    amount = newString.toLong()
                }
            },
            label = { androidx.compose.material3.Text("Amount") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )

        // team Leader
        androidx.compose.material3.TextField(
            value = teamLeader,
            onValueChange = { teamLeader = it },
            label = { androidx.compose.material3.Text("Team Leader") },
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )

        // team leader email
        androidx.compose.material3.TextField(
            value = email,
            onValueChange = {
                email = it
                isEmailValid = it.isBlank() || it.matches(emailRegex)
            },
            label = { androidx.compose.material3.Text("Team Leader Email") },
            modifier = Modifier.fillMaxWidth().padding(4.dp),
            isError = email.isNotBlank() && !email.matches(emailRegex),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = if (email.matches(emailRegex)) Color.Green else Color.Red
            ),
            singleLine = true
        )

        // Telephone
        androidx.compose.material3.TextField(
            value = telephone,
            onValueChange = { telephone = it },
            label = { androidx.compose.material3.Text("Telephone") },
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )

        // status
        androidx.compose.material3.TextField(
            value = status,
            onValueChange = { status = it },
            label = { androidx.compose.material3.Text("Status") },
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )

        // Created by
        TextField(
            value = createdBy,
            onValueChange = { createdBy = it },
            label = { Text("Created By") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        Button(
            onClick = {
                if (groupName == "") {
                    missingFields.add("Group Name Missing")
                }

                if (groupId == "") {
                    missingFields.add("Group ID Missing")
                }

                if (projNo == "") {
                    missingFields.add("Project Number Missing")
                }

                if (projName == "") {
                    missingFields.add("Project Name Missing")
                }

                if (projFocus == "") {
                    missingFields.add("Project Focus Missing")
                }

                if (projDesc == "") {
                    missingFields.add("Project Description Missing")
                }

                if (Dates.pdmStartDate.isNullOrBlank()) {
                    missingFields.add("Project Start Date Missing")
                }

                if (Dates.pdmEndDate.isNullOrBlank()) {
                    missingFields.add("Project End Date Missing")
                }

                if (fundedBy == "") {
                    missingFields.add("FundedBy Missing")
                }

                if (amount == 0L) {
                    missingFields.add("Amoun Missing")
                }

                if (teamLeader == "") {
                    missingFields.add("Teamleader Missing")
                }

                if (email == "") {
                    missingFields.add("Email Missing")
                }

                if (telephone == "") {
                    missingFields.add("Telephone Missing")
                }

                if (status == "") {
                    missingFields.add("Status Missing")
                }

                if (createdBy == "") {
                    missingFields.add("Status Missing")
                }

                if (missingFields.isNotEmpty()) {
                    showDialog.value = true
                    val missingFieldsMessage = "Please provide information for the following fields:\n${missingFields.joinToString(",\n")}"
                    errorMessage.value = missingFieldsMessage
                } else {
                    try {
                        // Get the current date and time
                        val currentDate = Date()

                        // Define a date format
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

                        // Format the current date and time into a string
                        val formattedDate = dateFormat.format(currentDate)

                        val geom = GeometryUtils.byteArrayToHexString(
                            GeometryUtils.createGeomFromLatLng(
                                UserLocation.lat,
                                UserLocation.long
                            )
                        )

                        val pdmProjectLocal = PdmProjectEntity(
                            id = 0,
                            geom = geom,
                            uuid = uuid,
                            groupId = groupId.toLong(),
                            groupName = groupName,
                            projNo = projNo,
                            projName = projName,
                            projFocus = projFocus,
                            projDesc = projDesc,
                            startDate = Dates.pdmStartDate,
                            endDate = Dates.pdmEndDate,
                            fundedBy = fundedBy,
                            amount = amount,
                            teamLeader = teamLeader,
                            email = email,
                            telephone = telephone,
                            status = status,
                            createdBy = createdBy,
                            dateCreated = formattedDate,
                            updatedBy = updatedBy,
                            dateUpdated = formattedDate,
                            lat = UserLocation.long,
                            longitude = UserLocation.lat,
                            uploaded = false
                        )

                        isUploading = true
                        projectLocalViewModel.savePdmProject(pdmProjectLocal)
                        isUploading = false
                        Toast.makeText(
                            context,
                            "Message: Project saved successfully!!",
                            Toast.LENGTH_LONG
                        ).show()
                        navController.navigate("SavedProjects")

                    } catch(e: Exception) {
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                        Log.e("Pdm Project Capture", "saving data failed due to ${e.message}")
                        throw RuntimeException("Capture: ${e.message}")
                    }

                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .padding(bottom = 20.dp)
        ){
            Text("Submit")
        }

        if (showDialog.value) {
            MissingFieldsDialog(mainDialog = showDialog, message = errorMessage, onDismiss = { /* handle dismiss */ })
        }

        LoadingDialog(
            isLoading = isUploading,
            msg = "Uploading Pdm Project...",
            onDismiss = { isUploading = false}
        )
    }


}
