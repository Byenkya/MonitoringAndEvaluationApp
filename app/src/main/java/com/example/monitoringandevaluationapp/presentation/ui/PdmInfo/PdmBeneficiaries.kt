package com.example.monitoringandevaluationapp.presentation.ui.PdmInfo

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.monitoringandevaluationapp.data.UserLocation
import com.example.monitoringandevaluationapp.data.api.model.ApiResponse
import com.example.monitoringandevaluationapp.data.api.model.Beneficiary
import com.example.monitoringandevaluationapp.data.repository.PostProjectBeneficiaryRepository
import com.example.monitoringandevaluationapp.domain.usecases.FetchAndPostBeneficiaryUseCaseImpl
import com.example.monitoringandevaluationapp.presentation.ui.CaptureData.DropdownWithLabel
import com.example.monitoringandevaluationapp.presentation.ui.CaptureData.MissingFieldsDialog
import com.example.monitoringandevaluationapp.presentation.ui.ProjectAssessments.LoadingDialog
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID

@Composable
fun BeneficiaryContentTab(navController: NavController) {
    val context = LocalContext.current
    var isUploading by remember { mutableStateOf(false) }
    var apiResponse = ApiResponse("")
    val scope = rememberCoroutineScope()
    val missingFields = mutableListOf<String>()
    val errorMessage = remember { mutableStateOf("") }
    val showDialog = remember { mutableStateOf(false) }
    var id by remember { mutableStateOf("") }
    var geom by remember { mutableStateOf("") }
    val uuid =  remember { UUID.randomUUID().toString() }
    var otherName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var memberId by remember { mutableStateOf("") }
    var nin by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    var subsistence by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("") }
    var subcounty by remember { mutableStateOf("") }
    var parish by remember { mutableStateOf("") }
    var village by remember { mutableStateOf("") }
    var createdBy by remember { mutableStateOf("") }
    var dateCreated by remember { mutableStateOf("") }
    var updatedBy by remember { mutableStateOf("") }
    var dateUpdated by remember { mutableStateOf("") }
    var groupName by remember { mutableStateOf("") }
    var groupId by remember { mutableStateOf("") }
    var latX by remember { mutableStateOf("") }
    var lonY by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState())
    ) {
        TextField(
            value = id,
            onValueChange = { newValue -> id = newValue.filter { it.isDigit() } },
            label = { Text("ID") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

//        TextField(
//            value = geom,
//            onValueChange = { geom = it },
//            label = { Text("Geom") },
//            modifier = Modifier.fillMaxWidth()
//        )

//        androidx.compose.material3.Text(
//            text = "Uuid: $uuid", fontWeight = FontWeight.Bold,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(4.dp)
//        )

        TextField(
            value = otherName,
            onValueChange = { otherName = it },
            label = { Text("Other Names") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        TextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        TextField(
            value = memberId,
            onValueChange = { newValue -> memberId = newValue.filter { it.isDigit() } },
            label = { Text("Memebr Id") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )


        TextField(
            value = nin,
            onValueChange = { nin = it },
            label = { Text("NIN") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        DropdownWithLabel(
            label = "Gender",
            suggestions = listOf("Male", "Female"),
            selectedText = gender,
            onTextSelected = { gender = it },
        )

        TextField(
            value = contact,
            onValueChange = { contact = it },
            label = { Text("Contact") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        TextField(
            value = status,
            onValueChange = { status = it },
            label = { Text("Status") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        TextField(
            value = subsistence,
            onValueChange = {newValue ->
                subsistence = newValue.filter { it.isDigit() }
            },
            label = { Text("Subsistance") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        // District
        DropdownWithLabel(
            label = "District",
            suggestions = listOf("Arua", "Madi Okollo", "Yumbe"),
            selectedText = district,
            onTextSelected = { district = it }
        )

        // subcounty
        DropdownWithLabel(
            label = "Sub County",
            suggestions = listOf("Adumi", "Ayivuni", "Pajulu"),
            selectedText = subcounty,
            onTextSelected = { subcounty = it }
        )

        // Parish
        DropdownWithLabel(
            label = "Parish",
            suggestions = listOf("Adumi", "Ayivuni", "Pajulu"),
            selectedText = parish,
            onTextSelected = { parish = it }
        )

        // village
        DropdownWithLabel(
            label = "Village",
            suggestions = listOf("Adumi", "Ayivuni", "Pajulu"),
            selectedText = village,
            onTextSelected = { village = it }
        )

        // groupName
        TextField(
            value = groupName,
            onValueChange = { groupName = it },
            label = { Text("Group Name") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        // groupID
        TextField(
            value = groupId,
            onValueChange = { newValue -> groupId = newValue.filter { it.isDigit() } },
            label = { Text("Group ID") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        TextField(
            value = createdBy,
            onValueChange = { createdBy = it },
            label = { Text("Created By") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        Button(
            onClick = {
                if (id == "") {
                    missingFields.add("Asset ID Missing")
                }

                if (otherName == "") {
                    missingFields.add("otherName Missing")
                }

                if (lastName == "") {
                    missingFields.add("lastName Missing")
                }

                if (lastName == "") {
                    missingFields.add("lastName Missing")
                }

                if (memberId == "") {
                    missingFields.add("MemberID Missing")
                }

                if (nin == "") {
                    missingFields.add("NIN Missing")
                }

                if (gender == "") {
                    missingFields.add("Gender Missing")
                }

                if (gender == "") {
                    missingFields.add("Gender Missing")
                }

                if (contact == "") {
                    missingFields.add("Contact Missing")
                }

                if (email == "") {
                    missingFields.add("Email Missing")
                }

                if (status == "") {
                    missingFields.add("Status Missing")
                }

                if (subsistence == "") {
                    missingFields.add("Subsistence Missing")
                }

                if (district == "") {
                    missingFields.add("District Missing")
                }

                if (subcounty == "") {
                    missingFields.add("SubCounty Missing")
                }

                if (parish == "") {
                    missingFields.add("Parish Missing")
                }

                if (village == "") {
                    missingFields.add("Village Missing")
                }

                if (createdBy == "") {
                    missingFields.add("Created By Missing")
                }

                if (groupName == "") {
                    missingFields.add("groupName Missing")
                }

                if (groupId == "") {
                    missingFields.add("groupID Missing")
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

                        val beneficiary = Beneficiary(
                            id = id.toLong(),
                            geom = "0101000020E610000036A2C56350D93E4052E656708E520840",
                            uuid = uuid,
                            other_name = otherName,
                            last_name = lastName,
                            member_id = memberId.toDouble(),
                            nin = nin,
                            gender = gender,
                            contact = contact,
                            email = email,
                            status = status,
                            subsistenc = subsistence.toLong(),
                            district = district,
                            subcounty = subcounty,
                            parish = parish,
                            village = village,
                            created_by = createdBy,
                            date_creat = formattedDate,
                            updated_by = updatedBy,
                            date_updat = formattedDate,
                            group_name = groupName,
                            group_id = groupId.toDouble(),
                            lat_x = UserLocation.lat,
                            lon_y = UserLocation.long
                        )

                        val postProjectBeneficiaryRepository = PostProjectBeneficiaryRepository(RetrofitClient.apiService)
                        val fetchAndPostBeneficiaryUseCaseImpl = FetchAndPostBeneficiaryUseCaseImpl(beneficiary, postProjectBeneficiaryRepository)
                        scope.launch {
                            isUploading = true
                            apiResponse = fetchAndPostBeneficiaryUseCaseImpl.execute()

                            if (apiResponse.message == "Beneficiary saved successfully!!") {
                                id = ""
                                geom = ""
                                otherName = ""
                                lastName = ""
                                memberId = ""
                                nin = ""
                                gender = ""
                                contact = ""
                                email = ""
                                status = ""
                                subsistence = ""
                                district = ""
                                subcounty = ""
                                parish = ""
                                village = ""
                                createdBy = ""
                                updatedBy = ""
                                groupName = ""
                                groupId = ""

                                isUploading = false
                                Toast.makeText(
                                    context,
                                    "Message: ${apiResponse.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                                navController.navigate("mapView")
                            } else {
                                isUploading = false
                                Toast.makeText(context, "Error: ${apiResponse.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    } catch(e: Exception) {
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                        Log.e("Asset Capture", "saving data failed due to ${e.message}")
                        throw RuntimeException("Capture: ${e.message}")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .padding(bottom = 20.dp) // Add bottom padding
        ) {
            Text("Submit")
        }

        if (showDialog.value) {
            MissingFieldsDialog(mainDialog = showDialog, message = errorMessage, onDismiss = { /* handle dismiss */ })
        }

        LoadingDialog(
            isLoading = isUploading,
            msg = "Uploading Beneficiary...",
            onDismiss = { isUploading = false}
        )


    }
}
