package com.example.monitoringandevaluationapp.presentation.ui.PdmInfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdmInfoTabView(
    navController: NavController,
    tabs: List<String>,
    content: @Composable (String) -> Unit
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val scrollState = rememberLazyListState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        androidx.compose.material3.TopAppBar(
            title = {
                androidx.compose.material3.Text(
                    "PDM Information",
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                androidx.compose.material3.IconButton(onClick = { navController.popBackStack() }) {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        )

        TabRow(
            selectedTabIndex = selectedTabIndex,
            backgroundColor = Color.White, // Set background color to white
            contentColor = Color.Black // Set text color to black
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title, fontSize = 8.sp, fontWeight = FontWeight.Bold,) },
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    modifier = Modifier.padding(horizontal = 0.dp, vertical = 0.dp)
                )
            }
        }

        // Content area for the selected tab
        content(tabs[selectedTabIndex])
    }
}
