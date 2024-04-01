package com.example.monitoringandevaluationapp.presentation.ui.PdmInfo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.monitoringandevaluationapp.presentation.ui.CaptureData.DropdownWithLabel

@Composable
fun BeneficiaryContentTab() {
    var id by remember { mutableStateOf("") }
    var geom by remember { mutableStateOf("") }
    var uuid by remember { mutableStateOf("") }
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

        TextField(
            value = uuid,
            onValueChange = { uuid = it },
            label = { Text("UUID") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = otherName,
            onValueChange = { otherName = it },
            label = { Text("Other Names") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = memberId,
            onValueChange = { memberId = it },
            label = { Text("Memebr Id") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = uuid,
            onValueChange = { uuid = it },
            label = { Text("UUID") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = nin,
            onValueChange = { nin = it },
            label = { Text("NIN") },
            modifier = Modifier.fillMaxWidth()
        )

        DropdownWithLabel(
            label = "Gender",
            suggestions = listOf("Male", "Female"),
            selectedText = gender,
            onTextSelected = { gender ->

            }
        )

        TextField(
            value = contact,
            onValueChange = { contact = it },
            label = { Text("Contact") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = status,
            onValueChange = { status = it },
            label = { Text("Status") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = subsistence,
            onValueChange = { subsistence = it },
            label = { Text("Subsistance") },
            modifier = Modifier.fillMaxWidth()
        )

        // District
        DropdownWithLabel(
            label = "District",
            suggestions = listOf("Adumi", "Ayivuni", "Pajulu"),
            selectedText = district,
            onTextSelected = { district ->

            }
        )

        // subcounty
        DropdownWithLabel(
            label = "Sub County",
            suggestions = listOf("Adumi", "Ayivuni", "Pajulu"),
            selectedText = subcounty,
            onTextSelected = { subcounty ->

            }
        )

        // Parish
        DropdownWithLabel(
            label = "Parish",
            suggestions = listOf("Adumi", "Ayivuni", "Pajulu"),
            selectedText = parish,
            onTextSelected = { parish ->
            }
        )

        // village
        DropdownWithLabel(
            label = "Village",
            suggestions = listOf("Adumi", "Ayivuni", "Pajulu"),
            selectedText = village,
            onTextSelected = { village ->

            }
        )

        TextField(
            value = createdBy,
            onValueChange = { createdBy = it },
            label = { Text("Created By") },
            modifier = Modifier.fillMaxWidth()
        )

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
