package com.example.monitoringandevaluationapp.presentation.ui.PdmInfo

import RetrofitClient
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.monitoringandevaluationapp.data.api.model.ApiResponse
import androidx.compose.foundation.text.KeyboardOptions
import androidx.navigation.NavController
import com.example.monitoringandevaluationapp.data.GroupEntity
import com.example.monitoringandevaluationapp.data.api.model.Group
import com.example.monitoringandevaluationapp.data.repository.PostProjectGroupRepository
import com.example.monitoringandevaluationapp.domain.usecases.FetchAndPostGroupUseCaseImpl
import com.example.monitoringandevaluationapp.presentation.ViewModel.GroupViewModel
import com.example.monitoringandevaluationapp.presentation.ui.CaptureData.MissingFieldsDialog
import com.example.monitoringandevaluationapp.presentation.ui.ProjectAssessments.LoadingDialog
import kotlinx.coroutines.launch
import retrofit2.Retrofit

@Composable
fun GroupsTabViewContent(navController: NavController, groupViewModel: GroupViewModel) {
    val context = LocalContext.current
    var isUploading by remember { mutableStateOf(false) }
    var apiResponse = ApiResponse("")
    val scope = rememberCoroutineScope()
    val missingFields = mutableListOf<String>()
    val errorMessage = remember { mutableStateOf("") }
    val showDialog = remember { mutableStateOf(false) }
    var id by remember { mutableStateOf("") }
    var groupName by remember { mutableStateOf("") }
    var descr by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        TextField(
            value = id,
            onValueChange = { newValue -> id = newValue.filter { it.isDigit() }},
            label = { Text("ID") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        TextField(
            value = groupName,
            onValueChange = { groupName = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        TextField(
            value = descr,
            onValueChange = { descr = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        Button(
            onClick = {
                if (id == "") {
                    missingFields.add("Group ID Missing")
                }

                if (groupName == "") {
                    missingFields.add("Group Name Missing")
                }

                if (descr == "") {
                    missingFields.add("Group Description Missing")
                }

                if (missingFields.isNotEmpty()) {
                    showDialog.value = true
                    val missingFieldsMessage = "Please provide information for the following fields:\n${missingFields.joinToString(",\n")}"
                    errorMessage.value = missingFieldsMessage
                } else {
                    try {

                        val group = Group(
                            id = id.toLong(),
                            name = groupName,
                            descr = descr
                        )

                        val groupEntity = GroupEntity(
                            id = 0,
                            name = groupName,
                            descr = descr
                        )

                        val postProjectGroupRepository = PostProjectGroupRepository(RetrofitClient.apiService)
                        val fetchAndPostGroupUseCaseImpl = FetchAndPostGroupUseCaseImpl(group, postProjectGroupRepository)

                        scope.launch {
                            isUploading = true
                            apiResponse = fetchAndPostGroupUseCaseImpl.execute()

                            if (apiResponse.message == "Group saved successfully!!") {
                                groupViewModel.saveProjectGroup(groupEntity)
                                id = ""
                                groupName = ""
                                descr = ""
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
                        Log.e("Group Capture", "saving data failed due to ${e.message}")
                        throw RuntimeException("Capture: ${e.message}")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .padding(bottom = 16.dp) // Add bottom padding
        ) {
            Text("Submit")
        }

        if (showDialog.value) {
            MissingFieldsDialog(mainDialog = showDialog, message = errorMessage, onDismiss = { /* handle dismiss */ })
        }

        LoadingDialog(
            isLoading = isUploading,
            msg = "Uploading Group...",
            onDismiss = { isUploading = false}
        )
    }
}
