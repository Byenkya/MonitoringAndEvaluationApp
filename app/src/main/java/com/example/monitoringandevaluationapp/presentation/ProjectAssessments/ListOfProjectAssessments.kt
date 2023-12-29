package com.example.monitoringandevaluationapp.presentation.ProjectAssessments

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import com.example.monitoringandevaluationapp.R
import com.example.monitoringandevaluationapp.usecases.LocationViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListOfProjectAssessments(
    navController: NavController,
    assessments: List<LocationEntity>,
    viewModel: LocationViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

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
            verticalArrangement = Arrangement.spacedBy(12.dp),
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
                            modifier = Modifier.weight(1f))

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
                                        Toast.makeText(
                                            context,
                                            "${location.projectName} pdf file has been downloaded successfully!!",
                                            Toast.LENGTH_LONG
                                        ).show()
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

                    LoadingDialog(isLoading = isLoading, onDismiss = { isLoading = false })
                }
            }


        }
    }
}

@Composable
fun LoadingDialog(isLoading: Boolean, onDismiss: () -> Unit) {
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
                    Text("Downloading PDF...", fontWeight = FontWeight.Medium)
                }
            },
            text = {},
            confirmButton = {},
            dismissButton = {}
        )
    }
}


