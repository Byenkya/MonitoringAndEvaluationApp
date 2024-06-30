package com.example.monitoringandevaluationapp.presentation.ui.SavedData

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.monitoringandevaluationapp.R
import com.example.monitoringandevaluationapp.data.LocationEntity
import com.example.monitoringandevaluationapp.data.PdmProjectEntity
import com.example.monitoringandevaluationapp.data.SavedAssessmentEntity
import com.example.monitoringandevaluationapp.data.api.model.ApiResponse
import com.example.monitoringandevaluationapp.domain.usecases.FetchAndPostProjectUseCaseImpl
import com.example.monitoringandevaluationapp.presentation.ViewModel.PdmProjectLocalViewModel
import com.example.monitoringandevaluationapp.data.api.model.PdmProject
import com.example.monitoringandevaluationapp.data.repository.PostProjectPdmRepository
import com.example.monitoringandevaluationapp.domain.usecases.FetchAndPostPdmProjectUseCaseImpl
import com.example.monitoringandevaluationapp.presentation.ui.CaptureData.MissingFieldsDialog
import com.example.monitoringandevaluationapp.presentation.ui.ProjectAssessments.LoadingDialog
import kotlinx.coroutines.launch
import androidx.navigation.NavOptions
import com.example.monitoringandevaluationapp.data.GeometryUtils
import com.example.monitoringandevaluationapp.data.UserLocation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedPdmProjects(navController: NavController, viewModel: PdmProjectLocalViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    val projects = remember { mutableStateListOf<PdmProjectEntity>() }
    val context = LocalContext.current

    LaunchedEffect(key1 = viewModel.allPdmLocalProjects) {
        viewModel.allPdmLocalProjects.observeForever { newList ->
            projects.clear()
            projects.addAll(newList)
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TopAppBar(
            title = { Text("Projects") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
        )

        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { newQuery ->
                searchQuery = newQuery
            },
            label = { Text("Search by Project Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.padding(bottom = 50.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(
                projects
                    .filter { it.projName.contains(searchQuery, ignoreCase = true) }
                    .distinctBy { it.projName }
                    .sortedByDescending { it.id }
            ) { project ->
                ProjectCard(viewModel = viewModel,navController = navController ,project = project, onItemClick = {
                    // Handle item click here, e.g., navigate to a detail screen
                    if(project.uploaded){
                        val navOptions = NavOptions.Builder()
                            .setEnterAnim(androidx.appcompat.R.anim.abc_slide_out_bottom)
                            .setExitAnim(androidx.appcompat.R.anim.abc_fade_out)
                            .build()
                        navController.navigate("projectDetails/${project.id}", navOptions)
                    } else {
                        Toast.makeText(context, "First Upload Project to view it's assessments!!", Toast.LENGTH_LONG).show()
                    }

                })
            }
        }
    }

}

@Composable
fun ProjectCard(viewModel: PdmProjectLocalViewModel,navController: NavController, project: PdmProjectEntity, onItemClick: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isUploading by remember { mutableStateOf(false) }
    var apiResponse = ApiResponse("")

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp) ,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth().clickable(onClick = onItemClick)
    ) {
        Column {

            // Text content inside a row with padding
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${project.projName}",
                    modifier = Modifier.weight(1f)
                )

                if(!project.uploaded) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.baseline_upload_24),
                        contentDescription = "Upload Pdm Project",
                        modifier = Modifier.clickable {
                            try {
                                val geom = GeometryUtils.byteArrayToHexString(
                                    GeometryUtils.createGeomFromLatLng(
                                        UserLocation.lat,
                                        UserLocation.long
                                    )
                                )
                                val pdmProject = PdmProject(
                                    geom = geom,
                                    uuid = project.uuid,
                                    group_id = project.groupId,
                                    group_name = project.groupName,
                                    proj_no = project.projNo,
                                    proj_name = project.projName,
                                    proj_focus = project.projFocus,
                                    proj_descr = project.projDesc,
                                    start_date = project.startDate,
                                    end_date = project.endDate,
                                    funded_by = project.fundedBy,
                                    amount_ugx = project.amount,
                                    team_leader = project.teamLeader,
                                    email = project.email,
                                    telephone = project.telephone,
                                    status = project.status,
                                    created_by = project.createdBy,
                                    date_creat = project.dateCreated,
                                    updated_by = project.updatedBy,
                                    date_updat = project.dateUpdated,
                                    lat_x = project.lat,
                                    lon_y = project.longitude
                                )

                                val postProjectPdmRepository = PostProjectPdmRepository(RetrofitClient.apiService)
                                val fetchAndPostPdmProjectUseCaseImpl = FetchAndPostPdmProjectUseCaseImpl(pdmProject, postProjectPdmRepository)

                                scope.launch {
                                    isUploading = true
                                    apiResponse = fetchAndPostPdmProjectUseCaseImpl.execute()

                                    if (apiResponse.message == "Pdm Project saved successfully!!") {
                                        viewModel.markProjectAsUploaded(project.id)
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
                                Log.e("Pdm Project Capture", "saving data failed due to ${e.message}")
                                throw RuntimeException("Capture: ${e.message}")
                            }
                        }
                    )
                }


                Spacer(modifier = Modifier.width(6.dp))

                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Navigate",
                    modifier = Modifier.clickable {
                        // Handle item click here, e.g., navigate to a detail screen
                        navController.navigate("projectAssessments/${project.projName}")
                    }
                )
            }

            LoadingDialog(
                isLoading = isUploading,
                msg = "Uploading Pdm Project...",
                onDismiss = { isUploading = false}
            )
        }
    }
}