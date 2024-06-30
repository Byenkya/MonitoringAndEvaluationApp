package com.example.monitoringandevaluationapp.presentation.ui.ProjectAssessments

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.monitoringandevaluationapp.R
import com.example.monitoringandevaluationapp.data.LocationEntity
import com.example.monitoringandevaluationapp.data.PdmProjectAssessmentEntity
import com.example.monitoringandevaluationapp.data.SavedAssessmentEntity
import com.example.monitoringandevaluationapp.data.api.model.ApiResponse
import com.example.monitoringandevaluationapp.data.repository.PdmProjectAssessmentRepository
import com.example.monitoringandevaluationapp.data.repository.PostPdmProjectAssessmentRepository
import com.example.monitoringandevaluationapp.domain.usecases.FetchAndPostPdmProjectAssessmentUseCaseImpl
import com.example.monitoringandevaluationapp.domain.usecases.FetchAndPostProjectUseCaseImpl
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdmProjectAssessments(
    navController: NavController,
    assessments: List<PdmProjectAssessmentEntity>,
    pdmProjectAssessmentRepository: PdmProjectAssessmentRepository,
    postPdmProjectAssessmentRepository: PostPdmProjectAssessmentRepository
){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isUploading by remember { mutableStateOf(false) }
    var apiResponse = ApiResponse("")

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
            ) {projectAssessment ->
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
                            text = "${projectAssessment.projectName} assessed on ${projectAssessment.dateAssessed} by ${projectAssessment.assessedBy}",
                            modifier = Modifier.weight(1f)
                        )

                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.baseline_upload_24),
                            contentDescription = "Upload Assessment",
                            modifier = Modifier.clickable {
                                try {
                                    if (!projectAssessment.uploaded){
                                        isUploading = true
                                        // Create an instance of FetchAndPostPdmProjectAssessmentUseCaseImpl
                                        val fetchAndPostPdmProjectAssessmentUseCaseImpl = FetchAndPostPdmProjectAssessmentUseCaseImpl(projectAssessment,postPdmProjectAssessmentRepository)
                                        scope.launch {
                                            apiResponse = fetchAndPostPdmProjectAssessmentUseCaseImpl.execute()
                                            isUploading = false

                                            if (apiResponse.message == "Project Assessment Saved successfully!") {
                                                pdmProjectAssessmentRepository.markProjectAssessmentAsUploaded(projectAssessment.id)
                                                Toast.makeText(
                                                    context,
                                                    "Message: ${apiResponse.message}",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                                pdmProjectAssessmentRepository.deleteAssessment(projectAssessment)
                                                navController.popBackStack()
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    "Message: ${apiResponse.message}",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                                navController.popBackStack()
                                            }
                                        }
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Project Assessment Already Uploaded",
                                            Toast.LENGTH_LONG
                                        ).show()
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