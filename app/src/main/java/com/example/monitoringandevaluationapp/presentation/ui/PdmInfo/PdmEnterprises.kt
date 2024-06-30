package com.example.monitoringandevaluationapp.presentation.ui.PdmInfo

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import com.example.monitoringandevaluationapp.data.DistrictDetails
import com.example.monitoringandevaluationapp.data.GeometryUtils
import com.example.monitoringandevaluationapp.data.GroupEntity
import com.example.monitoringandevaluationapp.data.PdmProjectEntity
import com.example.monitoringandevaluationapp.data.UserLocation
import com.example.monitoringandevaluationapp.data.api.model.ApiResponse
import com.example.monitoringandevaluationapp.data.api.model.Enterprise
import com.example.monitoringandevaluationapp.data.repository.PostProjectEnterpriseRepository
import com.example.monitoringandevaluationapp.domain.usecases.FetchAndPostEnterpriseUseCaseImpl
import com.example.monitoringandevaluationapp.presentation.ViewModel.GroupViewModel
import com.example.monitoringandevaluationapp.presentation.ui.CaptureData.DropdownWithLabel
import com.example.monitoringandevaluationapp.presentation.ui.CaptureData.MissingFieldsDialog
import com.example.monitoringandevaluationapp.presentation.ui.ProjectAssessments.LoadingDialog
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID

@Composable
fun EnterpriseTabViewContent(navController: NavController, groupViewModel: GroupViewModel) {
    val context = LocalContext.current
    val groups = remember { mutableStateListOf<GroupEntity>() }
    var selectedGroup by remember { mutableStateOf("") }
    var isUploading by remember { mutableStateOf(false) }
    var apiResponse = ApiResponse("")
    val scope = rememberCoroutineScope()
    val missingFields = mutableListOf<String>()
    val errorMessage = remember { mutableStateOf("") }
    val showDialog = remember { mutableStateOf(false) }
    var id by remember { mutableStateOf("") }
    val uuid =  remember { UUID.randomUUID().toString() }
    var groupName by remember { mutableStateOf("") }
    var groupId by remember { mutableStateOf("") }
    var regStatus by remember { mutableStateOf("") }
    var activation by remember { mutableStateOf("") }
    var fundingType by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("") }
    var subcounty by remember { mutableStateOf("") }
    var parish by remember { mutableStateOf("") }
    var village by remember { mutableStateOf("") }
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
//        TextField(
//            value = id,
//            onValueChange = { newValue -> id = newValue.filter { it.isDigit() }},
//            label = { Text("ID") },
//            modifier = Modifier.fillMaxWidth(),
//            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
//        )

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

        // regStatus
        CustomDropdown(
            label = "Registration Status",
            options = listOf("REGISTERED", "NOT REGISTERED", "OTHER"),
            selectedOption = regStatus,
            onOptionSelected = { selectedOption ->
                regStatus = selectedOption
            }
        )

        // Activation
        CustomDropdown(
            label = "Activation",
            options = listOf("ACTIVE", "INACTIVE", "PENDING"),
            selectedOption = activation,
            onOptionSelected = { selectedOption ->
                activation = selectedOption
            }
        )

        // funding type
        CustomDropdown(
            label = "Funding Type",
            options = listOf("PDM", "OTHER"),
            selectedOption = fundingType,
            onOptionSelected = { selectedOption ->
                fundingType = selectedOption
            }
        )

        // amount
//        TextField(
//            value = amount,
//            onValueChange = { amount = it },
//            label = { Text("Amount(UGX)") },
//            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
//        )

        TextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount(UGX)") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        // District
        DropdownWithLabel(
            label = "District",
            suggestions = listOf("Arua", "Madi-Okollo", "Nebbi", "Yumbe"),
            selectedText = district,
            onTextSelected = { district = it }
        )

        // subcounty
//        DropdownWithLabel(
//            label = "Sub County",
//            suggestions = listOf("Adumi", "Ayivuni", "Pajulu"),
//            selectedText = subcounty,
//            onTextSelected = { subcounty = it }
//        )
        DropdownWithLabel(
            label = "Sub County",
            suggestions = DistrictDetails.subCountyParishes.keys.toList(),
            selectedText = subcounty,
            onTextSelected = { subcounty = it }
        )

        // Parish
//        DropdownWithLabel(
//            label = "Parish",
//            suggestions = listOf("Adumi", "Ayivuni", "Pajulu"),
//            selectedText = parish,
//            onTextSelected = { parish = it }
//        )

        DropdownWithLabel(
            label = "Parish",
            suggestions = DistrictDetails.subCountyParishes[subcounty] ?: emptyList(),
            selectedText = parish,
            onTextSelected = { parish = it }
        )

        // village
//        DropdownWithLabel(
//            label = "Village",
//            suggestions = listOf("Adumi", "Ayivuni", "Pajulu"),
//            selectedText = village,
//            onTextSelected = { village = it }
//        )

        DropdownWithLabel(
            label = "Address(Village)",
            suggestions = DistrictDetails.villages,
            selectedText = village,
            onTextSelected = { village = it }
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

                if (regStatus == "") {
                    missingFields.add("Reg Status Missing")
                }

                if (activation == "") {
                    missingFields.add("Activation Missing")
                }

                if (fundingType == "") {
                    missingFields.add("fundingType Missing")
                }

                if (amount == "") {
                    missingFields.add("Amount Missing")
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

                if (village == "") {
                    missingFields.add("Village Missing")
                }

                if (createdBy == "") {
                    missingFields.add("Created By Missing")
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

                        val enterprise = Enterprise(
                            geom = geom,
                            uuid = uuid,
                            group_name = groupName,
                            group_id = groupId.toDouble(),
                            reg_status = regStatus,
                            activation = activation,
                            fundig_typ = fundingType,
                            amount = amount,
                            district = district,
                            subcounty = subcounty,
                            parish = parish,
                            village = village,
                            created_by = createdBy,
                            date_creat = formattedDate,
                            updated_by = updatedBy,
                            date_updat = formattedDate,
                            lat_x = UserLocation.long,
                            lon_y = UserLocation.lat
                        )

                        val postProjectEnterpriseRepository = PostProjectEnterpriseRepository(RetrofitClient.apiService)
                        val fetchAndPostEnterpriseUseCaseImpl = FetchAndPostEnterpriseUseCaseImpl(enterprise, postProjectEnterpriseRepository)

                        scope.launch {
                            isUploading = true
                            apiResponse = fetchAndPostEnterpriseUseCaseImpl.execute()

                            if (apiResponse.message == "Enterprise saved successfully!!") {
                                id = ""
                                groupName = ""
                                groupId = ""
                                regStatus = ""
                                activation = ""
                                fundingType = ""
                                amount = ""
                                district = ""
                                subcounty = ""
                                parish = ""
                                village = ""
                                createdBy = ""
                                updatedBy = ""

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
                        Log.e("Enterprise Capture", "saving data failed due to ${e.message}")
                        throw RuntimeException("Capture: ${e.message}")
                    }
                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .padding(bottom = 20.dp)
        ) {
            Text("Submit")
        }

        if (showDialog.value) {
            MissingFieldsDialog(mainDialog = showDialog, message = errorMessage, onDismiss = { /* handle dismiss */ })
        }

        LoadingDialog(
            isLoading = isUploading,
            msg = "Uploading Enterprise...",
            onDismiss = { isUploading = false}
        )
    }
}

@Composable
fun CustomDropdown(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    val icon = Icons.Filled.ArrowDropDown

    Box {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = { /* No-op, as it's readOnly */ },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                }
                .padding(top = 8.dp),
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
            options.forEach { option ->
                DropdownMenuItem(onClick = {
                    onOptionSelected(option)
                    expanded = false
                }) {
                    Text(text = option)
                }
            }
        }
    }
}

