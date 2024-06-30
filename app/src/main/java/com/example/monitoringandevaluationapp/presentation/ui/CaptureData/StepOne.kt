package com.example.monitoringandevaluationapp.presentation.ui.CaptureData

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.monitoringandevaluationapp.R
import com.example.monitoringandevaluationapp.data.Dates
import com.example.monitoringandevaluationapp.data.DistrictDetails
import com.example.monitoringandevaluationapp.presentation.ViewModel.LocationViewModel
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepOne(locationViewModel: LocationViewModel) {
    val pattern = remember { Regex("^\\d+\$") }
    val groupName by locationViewModel.groupName.collectAsState()
    val mainActivity by locationViewModel.mainActivity.collectAsState()
    val foundingDate = rememberSaveable { mutableStateOf("") }
    val registeredAlready by locationViewModel.registeredAlready.collectAsState()
    val registrationNumber by locationViewModel.registrationNumber.collectAsState()
    val registrationDate = remember { mutableStateOf("") }
    val address by locationViewModel.address.collectAsState()
    val parish by locationViewModel.parish.collectAsState()
    val subCounty by locationViewModel.subCounty.collectAsState()
    val county by locationViewModel.county.collectAsState()
    val district by locationViewModel.district.collectAsState()
    val subRegion by locationViewModel.subRegion.collectAsState()
    val country by locationViewModel.country.collectAsState()
    val created by locationViewModel.createdBy.collectAsState()
    val creationOn = remember { mutableStateOf("") }

    Column(
        Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
    ) {
        Text("Group Information", fontSize = 18.sp)
        Divider(color = Color.Black, thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))

        TextField(
            value = groupName,
            onValueChange = { locationViewModel.updateGroupName(it) },
            label = { Text("Group Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        )

        TextField(
            value = mainActivity,
            onValueChange = { locationViewModel.updateMainActivity(it) },
            label = { Text("Description") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        )

        // Founding Date Picker
        DateFieldWithPicker(
            label = "Founding Date",
            dates = Dates,
            date = foundingDate,
            onDateSelected = { date ->
                locationViewModel.updateFoundingDate(date)
            }
        )

        // already Registered
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Registered Already?")
            Spacer(modifier = Modifier.width(8.dp))
            Checkbox(
                checked = registeredAlready,
                onCheckedChange = { checked ->
                    locationViewModel.updateRegisteredAlready(checked)
                }
            )
        }

        // registration number
        TextField(
            value = registrationNumber,
            onValueChange = {
                if(it.matches(pattern)){
                    locationViewModel.updateRegistrationNumber(it)
                }
            },
            label = { Text("Registration Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )

        // Registration Date
        DateFieldWithPicker(
            label = "Registration Date",
            dates = Dates,
            date = registrationDate,
            onDateSelected = { date ->
                locationViewModel.updateRegDate(date)
            }
        )

        // Country
        DropdownWithLabel(
            label = "Country",
            suggestions = listOf("Uganda"),
            selectedText = country,
            onTextSelected = { country ->
                locationViewModel.updateCountry(country)
            }
        )

        // Sub Region
        DropdownWithLabel(
            label = "Sub Region",
            suggestions = listOf("Northern", "Central", "Eastern"),
            selectedText = subRegion,
            onTextSelected = { subRegion ->
                locationViewModel.updateSubRegion(subRegion)
            }
        )

        // District
        DropdownWithLabel(
            label = "District",
            suggestions = listOf("Arua", "Madi-Okollo", "Nebbi", "Yumbe"),
            selectedText = district,
            onTextSelected = { district ->
                locationViewModel.updateDistrict(district)
            }
        )

        // County
        DropdownWithLabel(
            label = "County",
            suggestions = DistrictDetails.counties,
            selectedText = county,
            onTextSelected = { county ->
                locationViewModel.updateCounty(county)
            }
        )

        // SubCounty
        DropdownWithLabel(
            label = "SubCounty",
            suggestions = DistrictDetails.subCountyParishes.keys.toList(),
            selectedText = subCounty,
            onTextSelected = { subCounty ->
                locationViewModel.updateSubCounty(subCounty)
            }
        )

        // Parish
//        DropdownWithLabel(
//            label = "Parish",
//            suggestions = listOf("Adumi", "Ayivuni", "Pajulu"),
//            selectedText = parish,
//            onTextSelected = { parish ->
//                locationViewModel.updateParish(parish)
//            }
//        )

        // Parish Dropdown
        DropdownWithLabel(
            label = "Parish",
            suggestions = DistrictDetails.subCountyParishes[subCounty] ?: emptyList(),
            selectedText = parish,
            onTextSelected = { selectedParish ->
                locationViewModel.updateParish(selectedParish)
            }
        )


        // Address(village)
        DropdownWithLabel(
            label = "Address(Village)",
            suggestions = DistrictDetails.villages,
            selectedText = address,
            onTextSelected = { village ->
                locationViewModel.updateAddress(village)
            }
        )


        // CreatedBy
        TextField(
            value = created,
            onValueChange = { locationViewModel.updateCreatedBy(it) },
            label = { Text("Created By") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        )

        // Created On
        DateFieldWithPicker(
            label = "Created On",
            dates = Dates,
            date = creationOn,
            onDateSelected = { date ->
                locationViewModel.updateCreatedOn(date)
            }
        )


    }

}

@Composable
fun DateFieldWithPicker(
    label: String,
    dates: Dates,
    date: MutableState<String>,
    onDateSelected: (String) -> Unit
) {
    var showDatePicker by rememberSaveable { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_date_range_24),
            contentDescription = "Date Icon",
            modifier = Modifier
                .size(24.dp)
                .clickable { showDatePicker = true }
        )
        Text(text = "${label}: ", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.width(8.dp))

        // Display the selected date in a TextView
        Text(
            text = when (label) {
                "Founding Date" -> dates.foundingDates
                "Registration Date" -> dates.registrationDate
                "Created On" -> dates.creationDate
                "Date of Birth" -> dates.dob
                "Start Date" -> dates.startDate
                "End Date" -> dates.endDate
                "Expected End Date" -> dates.expectedEndDate
                "Assessment Date" -> dates.assessmentDate
                "Date" -> dates.milestoneAssessmentDate
                "Target Date" -> dates.targetDate
                "Date Acquired" -> dates.dateAcquired
                "Pdm Start Date" -> dates.pdmStartDate
                "Pdm End Date" -> dates.pdmEndDate
                else -> "four"
            },
            fontWeight = FontWeight.Bold
        )
    }

    if (showDatePicker) {
        DatePickerDialogComponent(
            date = date,
            onDateSelected = {
                onDateSelected(it)
                showDatePicker = false

                when (label) {
                    "Founding Date" -> {
                        dates.foundingDates = it
                    }
                    "Registration Date" -> {
                        dates.registrationDate = it
                    }
                    "Created On" -> {
                        dates.creationDate = it
                    }
                    "Date of Birth" -> {
                        dates.dob = it
                    }
                    "Start Date" -> {
                        dates.startDate = it
                    }
                    "End Date" -> {
                        dates.endDate = it
                    }
                    "Expected End Date" -> {
                        dates.expectedEndDate = it
                    }
                    "Assessment Date" -> {
                        dates.assessmentDate = it
                    }
                    "Date" -> {
                        dates.milestoneAssessmentDate = it
                    }
                    "Target Date" -> {
                        dates.targetDate = it
                    }

                    "Date Acquired" -> {
                        dates.dateAcquired = it
                    }

                    "Pdm Start Date" -> {
                        dates.pdmStartDate = it
                    }

                    "Pdm End Date" -> {
                        dates.pdmEndDate = it
                    }
                }
                date.value = it

            },
            onDismiss = { showDatePicker = false }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialogComponent(
    date: MutableState<String>,
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {

    val datePickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return true
        }
    })

    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(onClick = {
                onDateSelected(selectedDate)
                onDismiss()
            }) {
                Text(text = "OK")
            }
        },
        dismissButton = {
            Button(onClick = {
                onDismiss()
            }) {
                Text(text = "Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}

private fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy")
    return formatter.format(Date(millis))
}

@Composable
fun DropdownWithLabel(
    label: String,
    suggestions: List<String>,
    selectedText: String,
    onTextSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    var searchQuery by remember { mutableStateOf("") }

    val icon = Icons.Filled.ArrowDropDown

    Box{
        OutlinedTextField(
            value = selectedText,
            onValueChange = { /* No-op, as it's readOnly */ },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                },
            label = { Text(label) },
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = "DropDown",
                    modifier = Modifier.clickable { expanded = !expanded }
                )
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                .height(300.dp)
        ) {
            // Add a search TextField at the top
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                    },
                    placeholder = { Text("Search") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Filter suggestions based on the search query
            val filteredSuggestions = suggestions.filter {
                it.contains(searchQuery, ignoreCase = true)
            }

            // Display filtered suggestions
            filteredSuggestions.forEach { suggestion ->
                DropdownMenuItem(onClick = {
                    onTextSelected(suggestion)
                    expanded = false
                }) {
                    Text(text = suggestion)
                }
            }
        }
    }
}




