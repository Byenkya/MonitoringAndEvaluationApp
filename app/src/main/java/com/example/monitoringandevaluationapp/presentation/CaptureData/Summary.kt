package com.example.monitoringandevaluationapp.presentation.CaptureData

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.monitoringandevaluationapp.R
import com.example.monitoringandevaluationapp.data.Dates
import com.example.monitoringandevaluationapp.data.LocationEntity
import com.example.monitoringandevaluationapp.usecases.LocationViewModel
import com.google.android.gms.maps.model.LatLng

@Composable
fun Summary(locationViewModel: LocationViewModel, navController: NavController) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var userLocation by remember{ mutableStateOf(LatLng(0.0, 0.0)) }

    DisposableEffect(lifecycleOwner) {
        val observer = Observer<LatLng> { newLocation ->
            userLocation = newLocation
        }

        locationViewModel.userLocation.observe(lifecycleOwner, observer)

        onDispose {
            locationViewModel.userLocation.removeObserver(observer)
        }
    }

    val context = LocalContext.current
    val groupName by locationViewModel.groupName.collectAsState()
    val mainActivity by locationViewModel.mainActivity.collectAsState()
    val registeredAlready by locationViewModel.registeredAlready.collectAsState()
    val registrationNumber by locationViewModel.registrationNumber.collectAsState()
    val address by locationViewModel.address.collectAsState()
    val parish by locationViewModel.parish.collectAsState()
    val subCounty by locationViewModel.subCounty.collectAsState()
    val county by locationViewModel.county.collectAsState()
    val district by locationViewModel.district.collectAsState()
    val subRegion by locationViewModel.subRegion.collectAsState()
    val country by locationViewModel.country.collectAsState()
    val created by locationViewModel.createdBy.collectAsState()

    // step 2
    val uuid by locationViewModel.memberUuid.collectAsState()
    val memberShipNumber by locationViewModel.memberShipNumber.collectAsState()
    val firstName by locationViewModel.firstName.collectAsState()
    val lastName by locationViewModel.lastName.collectAsState()
    val otherName by locationViewModel.otherName.collectAsState()
    val selectedGender by locationViewModel.gender.collectAsState()
    val paidSubscription by locationViewModel.paidSubscription.collectAsState()
    val memberRole by locationViewModel.memberRole.collectAsState()
    val memberImageUri by locationViewModel.memberImageUri.collectAsState()
    val otherDetails by locationViewModel.otherDetails.collectAsState()

    // step 3
    val projectNumber by locationViewModel.projectNumber.collectAsState()
    val projectName by locationViewModel.projectName.collectAsState()
    val projectFocus by locationViewModel.projectFocus.collectAsState()
    val fundedBy by locationViewModel.fundedBy.collectAsState()
    val amount by locationViewModel.amount.collectAsState()
    val teamLeader by locationViewModel.teamLeader.collectAsState()
    val teamLeaderEmail by locationViewModel.teamLeaderEmail.collectAsState()
    val teamLeaderPhone by locationViewModel.teamLeaderPhone.collectAsState()
    val otherProjectContacts by locationViewModel.otherProjectContacts.collectAsState()
    val projectDescription by locationViewModel.projectDescription.collectAsState()

    // step 4
    val assessedBy by locationViewModel.assessedBy.collectAsState()
    val assessMilestones by locationViewModel.assessMilestone.collectAsState()
    val assessmentFor by locationViewModel.assessmentFor.collectAsState()
    val obs by locationViewModel.obs.collectAsState()
    val photoOneUri by locationViewModel.photoOneUri.collectAsState()
    val photoTwoUri by locationViewModel.photoTwoUri.collectAsState()
    val photoThreeUri by locationViewModel.photoThreeUri.collectAsState()
    val photoFourUri by locationViewModel.photoFourUri.collectAsState()
    val lat by locationViewModel.lat.collectAsState()
    val long by locationViewModel.long.collectAsState()
    val altitude by locationViewModel.altitude.collectAsState()
    val gps by locationViewModel.gps.collectAsState()

    // step 5
    val milestoneDetails by locationViewModel.milestoneDetails.collectAsState()
    val milestoneTarget by locationViewModel.mileStoneTarget.collectAsState()
    val assignedTo by locationViewModel.assignedTo.collectAsState()
    val milestoneStatus by locationViewModel.milestoneStatus.collectAsState()
    val milestoneComments by locationViewModel.milestoneComments.collectAsState()
    val assessMilestone by locationViewModel.assessMilestone.collectAsState()
    val milestonePhotoOneUri by locationViewModel.milestonePhotoOneUri.collectAsState()
    val milestonePhotoTwoUri by locationViewModel.milestonePhotoTwoUri.collectAsState()
    val milestonePhotoThreeUri by locationViewModel.milestonePhotoThreeUri.collectAsState()
    val milestonePhotoFourUri by locationViewModel.milestonePhotoFourUri.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start
    ) {
        // Displaying general information
        Text("Group Information", fontSize = 18.sp)
        Divider(color = Color.Black, thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))

        Text("Group Name: $groupName")
        Text("Group Description")
        Text("$mainActivity", modifier = Modifier.border(1.dp, Color.Black))

        DateRow(label = "Founding Date", date = Dates.foundingDates)

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Registered Already?")
            Spacer(modifier = Modifier.width(8.dp))
            Checkbox(checked = registeredAlready, onCheckedChange = null)
        }

        Text("Registration Number: $registrationNumber")

        DateRow(label = "Registration Date: ", date = Dates.registrationDate)

        Text("Address(Village): $address")

        Text("Parish: $parish")

        Text("SubCounty: $subCounty")

        Text("County: $county")

        Text("District: $district")

        Text("Sub Region: $subRegion")

        Text("Country: $country")

        Text("Created By: $created")

        DateRow(label = "Created On: ", date = Dates.creationDate)

        // Displaying member information
        Text("Member Information", fontSize = 18.sp)
        Divider(color = Color.Black, thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))

        Text("Uuid: $uuid")

        Text("MemberShip Number: $memberShipNumber")

        Text("Member Names: $firstName $lastName")

        Text("Other Names: $otherName")

        Text("Gender: $selectedGender")

        DateRow(label = "Date of Birth: ", date = Dates.dob)

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Paid Subscription")
            Spacer(modifier = Modifier.width(8.dp))
            Checkbox(checked = paidSubscription, onCheckedChange = null)
        }

        Text("Member Role: $memberRole")

        if (memberImageUri?.path?.isNotEmpty() == true) {
            Image(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .border(2.dp, Color.Gray),
                painter = rememberAsyncImagePainter(memberImageUri),
                contentDescription = null
            )
        }

        Text("Other Details")
        Text("$otherDetails", modifier = Modifier.border(1.dp, Color.Black))

        // Displaying project information
        Text("Project Information", fontSize = 18.sp)
        Divider(color = Color.Black, thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))

        Text("Project Number: $projectNumber")

        Text("Project Name: $projectName")

        Text("Project Focus: $projectFocus")

        DateRow(label = "Start Date: ", date = Dates.startDate)

        DateRow(label = "End Date: ", date = Dates.endDate)

        DateRow(label = "Expected End Date: ", date = Dates.expectedEndDate)

        Text("Funded By: $fundedBy")

        Text("Amount: $amount")

        Text("Team Leader(TL): $teamLeader")

        Text("TL Email: $teamLeaderEmail")

        Text("TL Phone: $teamLeaderPhone")

        Text("Other Project Contacts: $otherProjectContacts")

        Text("Project Description")
        Text("$projectDescription" , modifier = Modifier.border(1.dp, Color.Black))

        // Displaying assessment information
        Text("Milestone Information", fontSize = 18.sp)
        Divider(color = Color.Black, thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))

        DateRow(label = "Assessment Date: ", date = Dates.assessmentDate)

        Text("Assessed By: $assessedBy")

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Assess Milestone")
            Spacer(modifier = Modifier.width(8.dp))
            Checkbox(checked = assessMilestone, onCheckedChange = null)
        }

        Text("Assessment For: $assessmentFor")

        Text("Observation")
        Text("$obs")

        Text("Photo One")
        if (photoOneUri?.path?.isNotEmpty() == true) {
            Image(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .border(2.dp, Color.Gray),
                painter = rememberAsyncImagePainter(photoOneUri),
                contentDescription = null
            )
        }

        Text("Photo Two")
        if (photoTwoUri?.path?.isNotEmpty() == true) {
            Image(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .border(2.dp, Color.Gray),
                painter = rememberAsyncImagePainter(photoTwoUri),
                contentDescription = null
            )
        }

        Text("Photo Three")
        if (photoThreeUri?.path?.isNotEmpty() == true) {
            Image(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .border(2.dp, Color.Gray),
                painter = rememberAsyncImagePainter(photoThreeUri),
                contentDescription = null
            )
        }

        Text("Photo Four")
        if (photoFourUri?.path?.isNotEmpty() == true) {
            Image(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .border(2.dp, Color.Gray),
                painter = rememberAsyncImagePainter(photoFourUri),
                contentDescription = null
            )
        }

        Text("Latitude: $lat")

        Text("Longitude: $long")

        Text("Altitude: $altitude")

        Text("Gps Crs: $gps")

        // display milestone information
        DateRow(label = "Milestone Assessment Date: ", date = Dates.milestoneAssessmentDate)

        Text("Milestone Details")
        Text("$milestoneDetails", modifier = Modifier.border(1.dp, Color.Black))

        Text("Milestone Target: $milestoneTarget")

        DateRow(label = "Target Date: ", date = Dates.targetDate)

        Text("Assigned To: $assignedTo")

        Text("Status: $milestoneStatus")

        Text("Comments")
        Text("$milestoneComments", modifier = Modifier.border(1.dp, Color.Black))

        Text("Photo Milestone Photo One")
        if (milestonePhotoOneUri?.path?.isNotEmpty() == true) {
            Image(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .border(2.dp, Color.Gray),
                painter = rememberAsyncImagePainter(milestonePhotoOneUri),
                contentDescription = null
            )
        }

        Text("Photo Milestone Photo Two")
        if (milestonePhotoTwoUri?.path?.isNotEmpty() == true) {
            Image(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .border(2.dp, Color.Gray),
                painter = rememberAsyncImagePainter(milestonePhotoTwoUri),
                contentDescription = null
            )
        }

        Text("Photo Milestone Photo Three")
        if (milestonePhotoThreeUri?.path?.isNotEmpty() == true) {
            Image(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .border(2.dp, Color.Gray),
                painter = rememberAsyncImagePainter(milestonePhotoThreeUri),
                contentDescription = null
            )
        }

        Text("Photo Milestone Photo Four")
        if (milestonePhotoFourUri?.path?.isNotEmpty() == true) {
            Image(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .border(2.dp, Color.Gray),
                painter = rememberAsyncImagePainter(milestonePhotoFourUri),
                contentDescription = null
            )
        }


        // Save button
        Button(onClick = {
           try {
               val saveMemberPhoto = saveFileToDownloads(context, memberImageUri!!)
               val savePhotoOne = saveFileToDownloads(context, photoOneUri!!)
               val savePhotoTwo = saveFileToDownloads(context, photoTwoUri!!)
               val savePhotoThree = saveFileToDownloads(context, photoThreeUri!!)
               val savePhotoFour = saveFileToDownloads(context, photoFourUri!!)
               val saveMilestonePhotoOne = saveFileToDownloads(context, milestonePhotoOneUri!!)
               val saveMilestonePhotoTwo = saveFileToDownloads(context, milestonePhotoTwoUri!!)
               val saveMilestonePhotoThree = saveFileToDownloads(context, milestonePhotoThreeUri!!)
               val saveMilestonePhotoFour = saveFileToDownloads(context, milestonePhotoFourUri!!)

               val locationEntity = LocationEntity(
                   id = 0,
                   groupName = groupName,
                   groupDescription = mainActivity,
                   foundingDate = Dates.foundingDates,
                   registered = registeredAlready,
                   registrationNumber = registrationNumber.toInt(),
                   registrationDate = Dates.registrationDate,
                   village = address,
                   parish = parish,
                   subCounty = subCounty,
                   county = county,
                   district = district,
                   subRegion = subRegion,
                   country = country,
                   createdBy = created,
                   createdOn = Dates.creationDate,
                   uuid = uuid,
                   memberShipNumber = memberShipNumber,
                   firstName = firstName,
                   lastName = lastName,
                   otherName = otherName,
                   gender = selectedGender,
                   dob = Dates.dob,
                   paid = paidSubscription,
                   memberRole = memberRole,
                   memberPhotoPath = saveMemberPhoto,
                   otherDetails = otherDetails,
                   projectNumber = projectNumber,
                   projectName = projectName,
                   projectFocus = projectFocus,
                   startDate = Dates.startDate,
                   endDate = Dates.endDate,
                   expectedDate = Dates.expectedEndDate,
                   fundedBy = fundedBy,
                   amount = amount,
                   teamLeader = teamLeader,
                   teamLeaderEmail = teamLeaderEmail,
                   teamLeaderPhone = teamLeaderPhone,
                   otherProjectContacts = otherProjectContacts,
                   projectDescription = projectDescription,
                   assessmentDate = Dates.assessmentDate,
                   assessMilestone = assessMilestones,
                   assessmentFor = assessmentFor,
                   observation = obs,
                   photoOnePath = savePhotoOne,
                   photoTwoPath = savePhotoTwo,
                   photoThreePath = savePhotoThree,
                   photoFourPath = savePhotoFour,
                   latitude = userLocation.latitude,
                   longitude = userLocation.longitude,
                   altitude = altitude,
                   gpsCrs = gps,
                   milestoneDate = Dates.milestoneAssessmentDate,
                   milestoneDetails = milestoneDetails,
                   milestoneTarget = milestoneTarget,
                   milestoneTargetDate = Dates.targetDate,
                   assignedTo = assignedTo,
                   status = milestoneStatus,
                   mileStoneComments = milestoneComments,
                   milestonePhotoOnePath = saveMilestonePhotoOne,
                   milestonePhotoTwoPath = saveMilestonePhotoTwo,
                   milestonePhotoThreePath = saveMilestonePhotoThree,
                   milestonePhotoFourPath = saveMilestonePhotoFour
               )

               locationViewModel.saveLocation(locationEntity)
               Toast.makeText(
                   context,
                   "Data saved successfully!!",
                   Toast.LENGTH_SHORT
               ).show()
               navController.navigate("SavedImages")

           } catch(e: Exception) {
               Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
               Log.e("Data Capture", "saving data failed due to ${e.message}")
               throw RuntimeException("Capture: ${e.message}")
           }
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
        ) {
            Text("Save")
        }
    }
}

@Composable
fun DateRow(label: String, date: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_date_range_24),
            contentDescription = "Date Icon",
            modifier = Modifier.size(24.dp)
        )
        Text(text = "$label: ", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = date, fontWeight = FontWeight.Bold)
    }
}
