package com.example.monitoringandevaluationapp.presentation.ui.PdmInfo

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EnterpriseTabViewContent() {
    var id by remember { mutableStateOf("") }
    var geom by remember { mutableStateOf("") }
    var uuid by remember { mutableStateOf("") }
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

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        TextField(
            value = id,
            onValueChange = { id = it },
            label = { Text("ID") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = geom,
            onValueChange = { geom = it },
            label = { Text("Geom") },
            modifier = Modifier.fillMaxWidth()
        )

        // Repeat for other fields...

        Button(
            onClick = { /* Handle form submission */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .padding(bottom = 16.dp) // Add bottom padding
        ) {
            Text("Submit")
        }
    }
}
