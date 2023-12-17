package com.example.monitoringandevaluationapp.presentation.CaptureData

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.monitoringandevaluationapp.data.Dates
import com.example.monitoringandevaluationapp.usecases.LocationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepThree(locationViewModel: LocationViewModel) {
    val pattern = remember { Regex("^\\d+\$") }
    val emailRegex = Regex("^\\S+@\\S+\\.\\S+$")
    var isEmailValid by remember { mutableStateOf(true) }
    val projectNumber by locationViewModel.projectNumber.collectAsState()
    val projectName by locationViewModel.projectName.collectAsState()
    val projectFocus by locationViewModel.projectFocus.collectAsState()
    val startDate = remember { mutableStateOf("") }
    val endDate = remember { mutableStateOf("") }
    val actualEndDate = remember { mutableStateOf("") }
    val fundedBy by locationViewModel.fundedBy.collectAsState()
    val amount by locationViewModel.amount.collectAsState()
    val teamLeader by locationViewModel.teamLeader.collectAsState()
    val teamLeaderEmail by locationViewModel.teamLeaderEmail.collectAsState()
    val teamLeaderPhone by locationViewModel.teamLeaderPhone.collectAsState()
    val otherProjectContacts by locationViewModel.otherProjectContacts.collectAsState()
    val projectDescription by locationViewModel.projectDescription.collectAsState()

    Column(
        Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
    ) {
        Text("Project Information", fontSize = 18.sp)
        Divider(color = Color.Black, thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))

        // project number
        TextField(
            value = projectNumber.toString(),
            onValueChange = { newString ->
                if (newString.matches(pattern)) {
                    locationViewModel.updateProjectNumber(newString.toLong())
                }
            },
            label = { Text("Enter Project Number") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )

        // projectName
        TextField(
            value = projectName,
            onValueChange = { locationViewModel.updateProjectName(it) },
            label = { Text("Project Name") },
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )

        // Project Focus
        TextField(
            value = projectFocus,
            onValueChange = { locationViewModel.updateProjectFocus(it) },
            label = { Text("Project Focus") },
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )

        // startDate
        DateFieldWithPicker(
            label = "Start Date",
            dates = Dates,
            date = startDate,
            onDateSelected = { date ->
                locationViewModel.updateStartDate(date)
            }
        )

        // EndDate
        DateFieldWithPicker(
            label = "End Date",
            dates = Dates,
            date = endDate,
            onDateSelected = { date ->
                locationViewModel.updateEndDate(date)
            }
        )

        // Actual End Date
        DateFieldWithPicker(
            label = "Expected End Date",
            dates = Dates,
            date = actualEndDate,
            onDateSelected = { date ->
                locationViewModel.updateExpectedEndDate(date)
            }
        )

        // FundedBy
        TextField(
            value = fundedBy,
            onValueChange = { locationViewModel.updateFundedBy(it) },
            label = { Text("Funded By") },
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )

        // Amount
        TextField(
            value = amount.toString(),
            onValueChange = { newString ->
                if (newString.matches(pattern)) {
                    locationViewModel.updateAmount(newString.toLong())
                }
            },
            label = { Text("Amount") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )

        // team Leader
        TextField(
            value = teamLeader,
            onValueChange = { locationViewModel.updateTeamLeader(it) },
            label = { Text("Team Leader") },
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )

        // team leader email
        TextField(
            value = teamLeaderEmail,
            onValueChange = {
                locationViewModel.updateTeamLeaderEmail(it)
                isEmailValid = it.isBlank() || it.matches(emailRegex)
            },
            label = { Text("Team Leader Email") },
            modifier = Modifier.fillMaxWidth().padding(4.dp),
            isError = teamLeaderEmail.isNotBlank() && !teamLeaderEmail.matches(emailRegex),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = if (teamLeaderEmail.matches(emailRegex)) Color.Green else Color.Red
            ),
            singleLine = true
        )

        if (!isEmailValid) {
            Text(
                text = "Invalid email format",
                color = Color.Red,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        // team leader phone
        TextField(
            value = teamLeaderPhone,
            onValueChange = { locationViewModel.updateTeamLeaderPhone(it) },
            label = { Text("Team Leader Phone") },
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )

        // other project contacts
        TextField(
            value = otherProjectContacts,
            onValueChange = { locationViewModel.updateOtherProjectContacts(it) },
            label = { Text("Other project Contacts") },
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )

        // project Description
        TextField(
            value = projectDescription,
            onValueChange = { locationViewModel.updateProjectDescription(it) },
            label = { Text("Project Description") },
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )

    }
}