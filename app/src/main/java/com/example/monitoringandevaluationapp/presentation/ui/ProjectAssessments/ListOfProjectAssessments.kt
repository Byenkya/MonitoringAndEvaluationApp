package com.example.monitoringandevaluationapp.presentation.ui.ProjectAssessments

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.monitoringandevaluationapp.data.LocationEntity
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.monitoringandevaluationapp.R
import com.example.monitoringandevaluationapp.data.SavedAssessmentEntity
import com.example.monitoringandevaluationapp.data.api.model.ApiResponse
import com.example.monitoringandevaluationapp.data.repository.PostProjectRepository
import com.example.monitoringandevaluationapp.domain.usecases.FetchAndPostProjectUseCaseImpl
import com.example.monitoringandevaluationapp.presentation.ViewModel.LocationViewModel
import com.example.monitoringandevaluationapp.presentation.ViewModel.SavedAssessmentViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListOfProjectAssessments(
    navController: NavController,
    assessments: List<LocationEntity>,
    assessmentCount: Int,
    viewModel: LocationViewModel,
    savedAssessmentViewModel: SavedAssessmentViewModel,
    projectRepository: PostProjectRepository
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isUploading by remember { mutableStateOf(false) }
    var apiResponse = ApiResponse("")
    // Observe download status
    val downloadStatus by viewModel.downloadStatus.collectAsState()

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

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TopAppBar(
            title = { Text("Project Assessments") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { newQuery ->
                searchQuery = newQuery
            },
            label = { Text("Search by Assessor") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.padding(bottom = 50.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                assessments
                    .filter { it.assessedBy.contains(searchQuery, ignoreCase = true) }
                    .sortedByDescending { it.id }
            ) { location ->
                // Simple card with Text and arrow icon
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
//                        .clickable { navController.navigate("projectDetails/${location.id}") }
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${location.projectName} assessed on ${location.assessmentDate} by ${location.assessedBy}",
                            modifier = Modifier.weight(1f)
                        )

                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.baseline_upload_24),
                            contentDescription = "Upload Assessment",
                            modifier = Modifier.clickable {
                                try {
                                    isUploading = true
                                    // Create an instance of FetchAndPostProjectUseCaseImpl
                                    val fetchAndPostProjectUseCase = FetchAndPostProjectUseCaseImpl(location, projectRepository)
                                    scope.launch {
                                        // Execute the use case
                                        if (assessmentCount == 1) {
                                            val assessmentDetails = savedAssessmentViewModel.getSavedProjectAssessmentById(
                                                location.id
                                            )
                                            if (assessmentDetails != null) {
                                                if(assessmentDetails.postedToServer) {
                                                    apiResponse = ApiResponse("Project Assessment Already Saved!!")
                                                    isUploading = false
                                                } else {
                                                    apiResponse = fetchAndPostProjectUseCase.execute()
                                                    isUploading = false
                                                }
                                            } else if (assessmentDetails == null) {
                                                apiResponse = fetchAndPostProjectUseCase.execute()
                                                isUploading = false
                                            }
                                        } else {
                                            apiResponse = fetchAndPostProjectUseCase.execute()
                                            isUploading = false
                                        }

                                        if (apiResponse.message == "Project Assessment Saved successfully!") {
                                            // Delete the location after a successful use case execution
                                            if (assessmentCount == 1) {
                                                val assessmentData = SavedAssessmentEntity(
                                                    0,
                                                    location.id,
                                                    true
                                                )
                                                savedAssessmentViewModel.saveProjectAssessment(
                                                    assessmentData
                                                )
                                                Toast.makeText(
                                                    context,
                                                    "Message: ${apiResponse.message}",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                                navController.popBackStack()
                                            } else {
                                                viewModel.deleteLocation(location)
                                                Toast.makeText(
                                                    context,
                                                    "Message: ${apiResponse.message}",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                                navController.popBackStack()
                                            }
                                        } else {
                                            // Handle the case where the use case was not successful
                                            Toast.makeText(
                                                context,
                                                "Failed to upload project Assessment: ${apiResponse.message}",
                                                Toast.LENGTH_LONG
                                            ).show()

                                            Log.e(">>>Upload error", " Failed to upload project Assessment ${apiResponse.message}")
                                        }
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        context,
                                        "Failed to upload project Assessment",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    Log.e("Upload error", " Failed to upload project Assessment", e)
                                }
                            }
                        )

                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.baseline_download_24),
                            contentDescription = "Download PDF",
                            modifier = Modifier.clickable {
                                // Trigger download PDF for the selected project
                                try {
                                    isLoading = true
                                    scope.launch {
                                        viewModel.downloadPDF(location)
                                        isLoading = false
                                        // Observe download status
                                        downloadStatus.let { success ->
                                            if (success) {
                                                // PDF download successful
                                                isLoading = false
                                                Toast.makeText(
                                                    context,
                                                    "Message: ${location.projectName} Assessment has been downloaded successfully!!",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            } else {
                                                // PDF download failed
                                                isLoading = false
                                                Toast.makeText(
                                                    context,
                                                    "Error: Failed to download PDF",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                        }
                                    }

                                } catch (e: Exception) {
                                    Toast.makeText(
                                        context,
                                        "Failed to download project PDF",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    Log.e("Download error", " Error while downloading project file", e)
                                }


                            }
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Navigate",
                            modifier = Modifier.clickable {
                                // Handle item click here, e.g., navigate to a detail screen
                                navController.navigate("projectDetails/${location.id}")
                            }
                        )
                    }

                    LoadingDialog(
                        isLoading = isLoading,
                        msg = "Downloading PDF...",
                        onDismiss = { isLoading = false }
                    )

                    LoadingDialog(
                        isLoading = isUploading,
                        msg = "Uploading Assessment...",
                        onDismiss = { isUploading = false}
                    )
                }
            }


        }
    }
}

@Composable
fun LoadingDialog(isLoading: Boolean, msg: String, onDismiss: () -> Unit) {
    if (isLoading) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(32.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(msg, fontWeight = FontWeight.Medium)
                }
            },
            text = {},
            confirmButton = {},
            dismissButton = {}
        )
    }
}


