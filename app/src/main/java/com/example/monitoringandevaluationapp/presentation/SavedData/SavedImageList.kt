package com.example.monitoringandevaluationapp.presentation.SavedData

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.monitoringandevaluationapp.data.LocationEntity
import com.example.monitoringandevaluationapp.usecases.LocationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedImageList(navController: NavController, viewModel: LocationViewModel) {
    val locations = remember { mutableStateListOf<LocationEntity>() }

    LaunchedEffect(key1 = viewModel.allLocations) {
        viewModel.allLocations.observeForever { newList ->
            locations.clear()
            locations.addAll(newList)
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TopAppBar(
            title = { Text("Saved Data") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        LazyColumn(
            modifier = Modifier.padding(bottom = 50.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(locations.sortedByDescending { it.id }) { location ->
                LocationCard(location)
            }
        }
    }


}

@Composable
fun LocationCard(location: LocationEntity) {
    // Add the 'file://' scheme to your image path
    val imagePathWithScheme = "file://${location.photoOnePath}"

    val painter = // Log or print the error here
        rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current).data(data = imagePathWithScheme).apply(block = fun ImageRequest.Builder.() {
                listener { _, throwable ->
                    throwable?.let {
                        // Log or print the error here
                        println("Error loading image: $it")
                    }
                }
            }).build()
        )

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp) ,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            // Image at the top, starts where the Card starts
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            // Text content inside a row with padding
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Project : ${location.projectName}", fontSize = 18.sp)
            }
        }
    }
}






