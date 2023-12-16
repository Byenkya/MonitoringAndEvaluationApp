package com.example.monitoringandevaluationapp.presentation.ProjectDetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.monitoringandevaluationapp.data.LocationEntity

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun ProjectDetails(navController: NavController, locationEntity: LocationEntity){
    if (locationEntity != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {
            TopAppBar(
                title = { Text("Project: ${locationEntity.projectName}", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
            Text("Group Information", fontSize = 18.sp)
            Divider(color = Color.Black, thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))

            Text("Group Name: ${locationEntity.groupName}")
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Group Description\n")
                    }
                    append("${locationEntity.groupDescription}")
                },
                textAlign = TextAlign.Start
            )

            DateWithIcon(
                date = "Founding Date: ${locationEntity.foundingDate}",
                icon = Icons.Default.DateRange,
                modifier = Modifier.padding(8.dp)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Registered Already?")
                Spacer(modifier = Modifier.width(8.dp))
                Checkbox(checked = locationEntity.registered, onCheckedChange = null)
            }

            Text("Registration Number: ${locationEntity.registrationNumber}")

            DateWithIcon(
                date = "Registration Date: ${locationEntity.registrationDate}",
                icon = Icons.Default.DateRange,
                modifier = Modifier.padding(8.dp)
            )

            Text("Address(Village): ${locationEntity.village}")

            Text("Parish: ${locationEntity.parish}")

            Text("SubCounty: ${locationEntity.subCounty}")

            Text("County: ${locationEntity.county}")

            Text("District: ${locationEntity.district}")

            Text("Sub Region: ${locationEntity.subRegion}")

            Text("Country: ${locationEntity.country}")

            Text("Created By: ${locationEntity.createdBy}")

            DateWithIcon(
                date = "Created On: ${locationEntity.createdOn}",
                icon = Icons.Default.DateRange,
                modifier = Modifier.padding(8.dp)
            )

            Text("Member Information", fontSize = 18.sp)
            Divider(color = Color.Black, thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))

            Text("Uuid: ${locationEntity.uuid}")

            Text("MemberShip Number: ${locationEntity.memberShipNumber}")

            Text("Member Names: ${locationEntity.lastName} ${locationEntity.firstName}")

            Text("Other Names: ${locationEntity.otherName}")

            Text("Gender: ${locationEntity.gender}")

            DateWithIcon(
                date = "Date of Birth: ${locationEntity.dob}",
                icon = Icons.Default.DateRange,
                modifier = Modifier.padding(8.dp)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Registered Already?")
                Spacer(modifier = Modifier.width(8.dp))
                Checkbox(checked = locationEntity.paid, onCheckedChange = null)
            }

            Text("Member Role: ${locationEntity.memberRole}\n")

            Text("Member Photo")

            if (locationEntity.memberPhotoPath.isNotEmpty()) {
                GlideImage(
                    model = locationEntity.memberPhotoPath,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .height(150.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Other Details\n")
                    }
                    append("${locationEntity.otherDetails}")
                },
                textAlign = TextAlign.Start
            )

            // Displaying project information
            Text("Project Information", fontSize = 18.sp)
            Divider(color = Color.Black, thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))

            Text("Project Number: ${locationEntity.projectNumber}")

            Text("Project Name: ${locationEntity.projectName}")

            Text("Project Focus: ${locationEntity.projectFocus}")

            DateWithIcon(
                date = "Start Date: ${locationEntity.startDate}",
                icon = Icons.Default.DateRange,
                modifier = Modifier.padding(8.dp)
            )

            DateWithIcon(
                date = "End Date: ${locationEntity.endDate}",
                icon = Icons.Default.DateRange,
                modifier = Modifier.padding(8.dp)
            )

            DateWithIcon(
                date = "Expected End Date: ${locationEntity.expectedDate}",
                icon = Icons.Default.DateRange,
                modifier = Modifier.padding(8.dp)
            )

            Text("Funded By: ${locationEntity.fundedBy}")

            Text("Amount: ${locationEntity.amount}")

            Text("Team Leader(TL): ${locationEntity.teamLeader}")

            Text("TL Email: ${locationEntity.teamLeaderEmail}")

            Text("TL Phone: ${locationEntity.teamLeaderPhone}")

            Text("Other Project Contacts: ${locationEntity.otherProjectContacts}")

            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Project Description\n")
                    }
                    append("${locationEntity.projectDescription}")
                },
                textAlign = TextAlign.Start
            )

            // Displaying assessment information
            Text("Milestone Information", fontSize = 18.sp)
            Divider(color = Color.Black, thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))

            DateWithIcon(
                date = "Assessed Date: ${locationEntity.assessmentDate}",
                icon = Icons.Default.DateRange,
                modifier = Modifier.padding(8.dp)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Milestone assessed?")
                Spacer(modifier = Modifier.width(8.dp))
                Checkbox(checked = locationEntity.assessMilestone, onCheckedChange = null)
            }

            Text("Assessment For: ${locationEntity.assessmentFor}")

            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Observation\n")
                    }
                    append("${locationEntity.observation}")
                },
                textAlign = TextAlign.Start
            )

            Text("Photo One")
            if (locationEntity.photoOnePath.isNotEmpty()) {
                GlideImage(
                    model = locationEntity.photoOnePath,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .height(150.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Text("Photo Two")
            if (locationEntity.photoTwoPath.isNotEmpty()) {
                GlideImage(
                    model = locationEntity.photoTwoPath,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .height(150.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Text("Photo Three")
            if (locationEntity.photoThreePath.isNotEmpty()) {
                GlideImage(
                    model = locationEntity.photoThreePath,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .height(150.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Text("Photo Four")
            if (locationEntity.photoFourPath.isNotEmpty()) {
                GlideImage(
                    model = locationEntity.photoFourPath,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .height(150.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Text("Latitude: ${locationEntity.latitude}")

            Text("Longitude: ${locationEntity.longitude}")

            Text("Altitude: ${locationEntity.altitude}")

            Text("Gps Crs: ${locationEntity.gpsCrs}")

            DateWithIcon(
                date = "Milestone Assessment Date: ${locationEntity.milestoneDate}",
                icon = Icons.Default.DateRange,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Milestone Details\n")
                    }
                    append("${locationEntity.milestoneDetails}")
                },
                textAlign = TextAlign.Start
            )

            Text("Milestone Target: ${locationEntity.milestoneTarget}")

            DateWithIcon(
                date = "Target Date: ${locationEntity.milestoneTargetDate}",
                icon = Icons.Default.DateRange,
                modifier = Modifier.padding(8.dp)
            )

            Text("Assigned To: ${locationEntity.assignedTo}")

            Text("Status: ${locationEntity.status}")
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Comments\n")
                    }
                    append("${locationEntity.mileStoneComments}")
                },
                textAlign = TextAlign.Start
            )

            Text("Photo Milestone Photo One")
            if (locationEntity.milestonePhotoOnePath.isNotEmpty()) {
                GlideImage(
                    model = locationEntity.milestonePhotoOnePath,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .height(150.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Text("Photo Milestone Photo Two")
            if (locationEntity.milestonePhotoTwoPath.isNotEmpty()) {
                GlideImage(
                    model = locationEntity.milestonePhotoTwoPath,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .height(150.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Text("Photo Milestone Photo Three")
            if (locationEntity.milestonePhotoThreePath.isNotEmpty()) {
                GlideImage(
                    model = locationEntity.milestonePhotoThreePath,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .height(150.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Text("Photo Milestone Photo Four")
            if (locationEntity.milestonePhotoFourPath.isNotEmpty()) {
                GlideImage(
                    model = locationEntity.milestonePhotoFourPath,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .height(150.dp),
                    contentScale = ContentScale.Crop
                )
            }
            
            Spacer(modifier = Modifier.height(70.dp))
        }
    }else {
        // Handle the case where locationEntity  is null
        Text(text = "Invalid location data")
    }
}

@Composable
fun DateWithIcon(date: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon
        Icon(
            imageVector = icon,
            contentDescription = null, // Content description can be null if the icon is decorative
            tint = Color.Gray, // Tint the icon with a specific color if needed
            modifier = Modifier.size(24.dp) // Set the size of the icon
        )

        // Spacer between icon and text
        Spacer(modifier = Modifier.width(4.dp))

        // Date text
        Text(
            text = date,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
            // Add other styling properties as needed
        )
    }
}
