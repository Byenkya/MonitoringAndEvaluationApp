package com.example.monitoringandevaluationapp.usecases

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.monitoringandevaluationapp.data.LocationEntity
import com.example.monitoringandevaluationapp.repository.LocationRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LocationViewModel(private val repository: LocationRepository) : ViewModel() {

    // LiveData to hold the list of LocationEntity
    val allLocations: LiveData<List<LocationEntity>> = repository.getAllLocations()

    fun saveLocation(locationEntity: LocationEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.saveLocation(locationEntity)
    }

    private val _userLocation = MutableLiveData<LatLng>()
    val userLocation: LiveData<LatLng> get() = _userLocation

    fun updateUserLocation(newLocation: LatLng) {
        _userLocation.value = newLocation
    }

    private val _projectTitle = MutableStateFlow("")
    val projectTitle get() = _projectTitle
    fun updateProjectTitle(title: String) {
        viewModelScope.launch {
            _projectTitle.value = title
        }
    }

    // group name
    private val _groupName = MutableStateFlow("")
    val groupName get() = _groupName
    fun updateGroupName(groupName: String) {
        viewModelScope.launch {
            _groupName.value = groupName
        }
    }

    // mainActivity
    private val _mainActivity = MutableStateFlow("")
    val mainActivity get() = _mainActivity
    fun updateMainActivity(mainActivity: String) {
        viewModelScope.launch {
            _mainActivity.value = mainActivity
        }
    }

    // foundingDate
    private val _foundingDate = MutableStateFlow("")
    val foundingDate get() = _foundingDate
    fun updateFoundingDate(date: String) {
        viewModelScope.launch {
            _foundingDate.value = date
        }
    }

    // registeredAlready
    private val _registeredAlready = MutableStateFlow(false)
    val registeredAlready get() = _registeredAlready
    fun updateRegisteredAlready(state: Boolean) {
        viewModelScope.launch {
            _registeredAlready.value = state
        }
    }

    // registrationNumber
    private val _registrationNumber = MutableStateFlow("")
    val registrationNumber get() = _registrationNumber
    fun updateRegistrationNumber(regNo: String) {
        viewModelScope.launch {
            _registrationNumber.value = regNo
        }
    }

    // registrationDate
    private val _regDate = MutableStateFlow("")
    val regDate get() = _regDate
    fun updateRegDate(date: String) {
        viewModelScope.launch {
            _regDate.value = date
        }
    }

    // Address(village)
    private val _address = MutableStateFlow("")
    val address get() = _address

    fun updateAddress(address: String) {
        viewModelScope.launch {
            _address.value = address
        }
    }

    // parish
    private val _parish = MutableStateFlow("")
    val parish get() = _parish

    fun updateParish(parish: String) {
        viewModelScope.launch {
            _parish.value = parish
        }
    }

    // subCounty
    private val _subCounty = MutableStateFlow("")
    val subCounty get() = _subCounty

    fun updateSubCounty(subCounty: String) {
        viewModelScope.launch {
            _subCounty.value = subCounty
        }
    }

    // county
    private val _county = MutableStateFlow("")
    val county get() = _county

    fun updateCounty(county: String) {
        viewModelScope.launch {
            _county.value = county
        }
    }

    // district
    private val _district = MutableStateFlow("")
    val district get() = _district

    fun updateDistrict(district: String) {
        viewModelScope.launch {
            _district.value = district
        }
    }

    // subRegion
    private val _subRegion = MutableStateFlow("")
    val subRegion get() = _subRegion

    fun updateSubRegion(subRegion: String) {
        viewModelScope.launch {
            _subRegion.value = subRegion
        }
    }

    // country
    private val _country = MutableStateFlow("Uganda")
    val country get() = _country

    fun updateCountry(country: String) {
        viewModelScope.launch {
            _country.value = country
        }
    }

    // createdBy
    private val _createdBy = MutableStateFlow("")
    val createdBy get() = _createdBy

    fun updateCreatedBy(name: String) {
        viewModelScope.launch {
            _createdBy.value = name
        }
    }

    // createdOn
    private val _createdOn = MutableStateFlow("")
    val createdOn get() = _createdOn

    fun updateCreatedOn(date: String) {
        viewModelScope.launch {
            _createdOn.value = date
        }
    }

    private val _capturedImageUri = MutableStateFlow<Uri?>(null)
    val capturedImageUri = _capturedImageUri.asStateFlow()

    fun updateCapturedImageUri(uri: Uri?) {
        viewModelScope.launch {
            _capturedImageUri.value = uri
        }

    }

    // step 2: Membership Information
    // Member uuid
    private val _memberUuid = MutableStateFlow("")
    val memberUuid get() = _memberUuid
    fun updateMemberUuid(uuid: String) {
        viewModelScope.launch {
            _memberUuid.value = uuid
        }
    }

    // Membership Number
    private val _memberShipNumber = MutableStateFlow(0)
    val memberShipNumber get() = _memberShipNumber
    fun updateMemberShipNumber(number: Int) {
        viewModelScope.launch {
            _memberShipNumber.value = number
        }
    }

    // First Name
    private val _firstName = MutableStateFlow("")
    val firstName get() = _firstName
    fun updateFirstName(name: String) {
        viewModelScope.launch {
            _firstName.value = name
        }
    }

    // Last Name
    private val _lastName = MutableStateFlow("")
    val lastName get() = _lastName
    fun updateLastName(name: String) {
        viewModelScope.launch {
            _lastName.value = name
        }
    }

    // Other name
    private val _otherName = MutableStateFlow("")
    val otherName get() = _otherName
    fun updateOtherName(name: String) {
        viewModelScope.launch {
            _otherName.value = name
        }
    }

    // Gender
    private val _gender = MutableStateFlow("")
    val gender get() = _gender
    fun updateGender(gender: String) {
        viewModelScope.launch {
            _gender.value = gender
        }
    }

    // Start Date
    private val _dob = MutableStateFlow("")
    val dob get() = _dob
    fun updateDOB(date: String) {
        viewModelScope.launch {
            _dob.value = date
        }
    }

    // Paid Subscription
    private val _paidSubscription = MutableStateFlow(false)
    val paidSubscription get() = _paidSubscription
    fun updatePaidSubscription(state: Boolean) {
        viewModelScope.launch {
            _paidSubscription.value = state
        }
    }

    // Member role
    private val _memberRole = MutableStateFlow("")
    val memberRole get() = _memberRole
    fun updateMemberRole(role: String) {
        viewModelScope.launch {
            _memberRole.value = role
        }
    }

    // MemberPhoto
    private val _memberImageUri = MutableStateFlow<Uri?>(null)
    val memberImageUri = _memberImageUri.asStateFlow()
    fun updateMemberImageUri(uri: Uri?) {
        viewModelScope.launch {
            _memberImageUri.value = uri
        }

    }

    // other details
    private val _otherDetails = MutableStateFlow("")
    val otherDetails get() = _otherDetails
    fun updateOtherDetails(details: String) {
        viewModelScope.launch {
            _otherDetails.value = details
        }
    }

    // step 3: project Information
    // projectNumber
    private val _projectNumber = MutableStateFlow(0)
    val projectNumber get() = _projectNumber
    fun updateProjectNumber(projectNumber: Int) {
        viewModelScope.launch {
            _projectNumber.value = projectNumber
        }
    }

    // ProjectName
    private val _projectName = MutableStateFlow("")
    val projectName get() = _projectName
    fun updateProjectName(name: String) {
        viewModelScope.launch {
            _projectName.value = name
        }
    }

    // Project Focus
    private val _projectFocus = MutableStateFlow("")
    val projectFocus get() = _projectFocus
    fun updateProjectFocus(focus: String) {
        viewModelScope.launch {
            _projectFocus.value = focus
        }
    }

    // Start Date
    private val _startDate = MutableStateFlow("")
    val startDate get() = _startDate
    fun updateStartDate(date: String) {
        viewModelScope.launch {
            _startDate.value = date
        }
    }

    // End Date
    private val _endDate = MutableStateFlow("")
    val endDate get() = _endDate
    fun updateEndDate(endDate: String) {
        viewModelScope.launch {
            _endDate.value = endDate
        }
    }

    // Expected End Date
    private val _expectedEndDate = MutableStateFlow("")
    val expectedEndDate get() = _expectedEndDate
    fun updateExpectedEndDate(date: String) {
        viewModelScope.launch {
            _expectedEndDate.value = date
        }
    }

    // Funded By
    private val _fundedBy = MutableStateFlow("")
    val fundedBy get() = _fundedBy
    fun updateFundedBy(fundedBy: String) {
        viewModelScope.launch {
            _fundedBy.value = fundedBy
        }
    }

    // Amount(UGX)
    private val _amount = MutableStateFlow(0L)
    val amount get() = _amount
    fun updateAmount(amount: Long) {
        viewModelScope.launch {
            _amount.value = amount
        }
    }

    // Team Leader
    private val _teamLeader = MutableStateFlow("")
    val teamLeader get() = _teamLeader
    fun updateTeamLeader(leader: String) {
        viewModelScope.launch {
            _teamLeader.value = leader
        }
    }

    // Team Leader Email
    private val _teamLeaderEmail = MutableStateFlow("")
    val teamLeaderEmail get() = _teamLeaderEmail
    fun updateTeamLeaderEmail(email: String) {
        viewModelScope.launch {
            _teamLeaderEmail.value = email
        }
    }

    // Team Leader Phone
    private val _teamLeaderPhone = MutableStateFlow("")
    val teamLeaderPhone get() = _teamLeaderPhone
    fun updateTeamLeaderPhone(phone: String) {
        viewModelScope.launch {
            _teamLeaderPhone.value = phone
        }
    }

    // Other project
    private val _otherProjectContacts = MutableStateFlow("")
    val otherProjectContacts get() = _otherProjectContacts
    fun updateOtherProjectContacts(contacts: String) {
        viewModelScope.launch {
            _otherProjectContacts.value = contacts
        }
    }

    // project description
    private val _projectDescription = MutableStateFlow("")
    val projectDescription get() = _projectDescription
    fun updateProjectDescription(newDesc: String) {
        viewModelScope.launch {
            _projectDescription.value = newDesc
        }

    }

    // step 4
    // Assessment Information
    private val _assessmentDate = MutableStateFlow("")
    val assessmentDate get() = _assessmentDate
    fun updateAssessmentDate(date: String) {
        viewModelScope.launch {
            _assessmentDate.value = date
        }
    }

    // AssessedBy
    private val _assessedBy = MutableStateFlow("")
    val assessedBy get() = _assessedBy
    fun updateAssessedBy(name: String) {
        viewModelScope.launch {
            _assessedBy.value = name
        }
    }

    // Assess Milestones
    private val _assessMilestone = MutableStateFlow(false)
    val assessMilestone get() = _assessMilestone
    fun updateAssessMilestone(state: Boolean) {
        viewModelScope.launch {
            _assessMilestone.value = state
        }
    }

    // Assessment for
    private val _assessmentFor = MutableStateFlow("")
    val assessmentFor get() = _assessmentFor
    fun updateAssessmentFor(assessmentFor: String) {
        viewModelScope.launch {
            _assessmentFor.value = assessmentFor
        }
    }

    // observation
    private val _obs = MutableStateFlow("")
    val obs get() = _obs
    fun updateObs(obs: String) {
        viewModelScope.launch {
            _obs.value = obs
        }
    }

    // photo 1
    private val _photoOneUri = MutableStateFlow<Uri?>(null)
    val photoOneUri = _photoOneUri.asStateFlow()
    fun updatePhotoOneUri(uri: Uri?) {
        viewModelScope.launch {
            _photoOneUri.value = uri
        }

    }

    // photo 2
    private val _photoTwoUri = MutableStateFlow<Uri?>(null)
    val photoTwoUri = _photoTwoUri.asStateFlow()
    fun updatePhotoTwoUri(uri: Uri?) {
        viewModelScope.launch {
            _photoTwoUri.value = uri
        }

    }

    // photo 3
    private val _photoThreeUri = MutableStateFlow<Uri?>(null)
    val photoThreeUri = _photoThreeUri.asStateFlow()
    fun updatePhotoThreeUri(uri: Uri?) {
        viewModelScope.launch {
            _photoThreeUri.value = uri
        }

    }

    // photo 4
    private val _photoFourUri = MutableStateFlow<Uri?>(null)
    val photoFourUri = _photoFourUri.asStateFlow()
    fun updatePhotoFourUri(uri: Uri?) {
        viewModelScope.launch {
            _photoFourUri.value = uri
        }

    }

    // lat
    private val _lat = MutableStateFlow(0.0)
    val lat get() = _lat
    fun updateLat(lat: Double) {
        viewModelScope.launch {
            _lat.value = lat
        }
    }

    // long
    private val _long = MutableStateFlow(0.0)
    val long get() = _long
    fun updateLong(long: Double) {
        viewModelScope.launch {
            _long.value = long
        }
    }

    // Altitude
    private val _altitude = MutableStateFlow("")
    val altitude get() = _altitude
    fun updateAltitude(altitude: String) {
        viewModelScope.launch {
            _altitude.value = altitude
        }
    }

    // GPS CRS
    private val _gps = MutableStateFlow("")
    val gps get() = _gps
    fun updateGps(gps: String) {
        viewModelScope.launch {
            _gps.value = gps
        }
    }

    // step 5
    // Milestones
    private val _mileStoneAssessmentDate = MutableStateFlow("")
    val mileStoneAssessmentDate get() = _mileStoneAssessmentDate
    fun updateMileStoneAssessmentDate(date: String) {
        viewModelScope.launch {
            _mileStoneAssessmentDate.value = date
        }
    }

    // milestone details
    private val _milestoneDetails = MutableStateFlow("")
    val milestoneDetails get() = _milestoneDetails
    fun updateMilestoneDetails(details: String) {
        viewModelScope.launch {
            _milestoneDetails.value = details
        }
    }

    // milestone target
    private val _milestoneTarget = MutableStateFlow("")
    val mileStoneTarget get() = _milestoneTarget
    fun updateMileStoneTarget(target: String) {
        viewModelScope.launch {
            _milestoneTarget.value = target
        }
    }

    // milestone date
    private val _mileStoneTargetDate = MutableStateFlow("")
    val mileStoneTargetDate get() = _mileStoneTargetDate
    fun updateMileStoneTargetDate(date: String) {
        viewModelScope.launch {
            _mileStoneTargetDate.value = date
        }
    }

    // assigned To
    private val _assignedTo = MutableStateFlow("")
    val assignedTo get() = _assignedTo
    fun updateAssignedTo(assignedTo: String) {
        viewModelScope.launch {
            _assignedTo.value = assignedTo
        }
    }

    // milestone status
    private val _milestoneStatus = MutableStateFlow("")
    val milestoneStatus get() = _milestoneStatus
    fun updateMilestoneStatus(status: String) {
        viewModelScope.launch {
            _milestoneStatus.value = status
        }
    }

    // milestone comments
    private val _milestoneComments = MutableStateFlow("")
    val milestoneComments get() = _milestoneComments
    fun updateMilestoneComments(comment: String) {
        viewModelScope.launch {
            _milestoneComments.value = comment
        }
    }

    // milestone photo 1
    private val _milestonePhotoOneUri = MutableStateFlow<Uri?>(null)
    val milestonePhotoOneUri = _milestonePhotoOneUri.asStateFlow()
    fun updateMilestonePhotoOneUri(uri: Uri?) {
        viewModelScope.launch {
            _milestonePhotoOneUri.value = uri
        }

    }

    // milestone photo 2
    private val _milestonePhotoTwoUri = MutableStateFlow<Uri?>(null)
    val milestonePhotoTwoUri = _milestonePhotoTwoUri.asStateFlow()
    fun updateMilestonePhotoTwoUri(uri: Uri?) {
        viewModelScope.launch {
            _milestonePhotoTwoUri.value = uri
        }

    }

    // milestone photo 3
    private val _milestonePhotoThreeUri = MutableStateFlow<Uri?>(null)
    val milestonePhotoThreeUri = _milestonePhotoThreeUri.asStateFlow()
    fun updateMilestonePhotoThreeUri(uri: Uri?) {
        viewModelScope.launch {
            _milestonePhotoThreeUri.value = uri
        }

    }

    // milestone photo 4
    private val _milestonePhotoFourUri = MutableStateFlow<Uri?>(null)
    val milestonePhotoFourUri = _milestonePhotoFourUri.asStateFlow()
    fun updateMilestonePhotoFourUri(uri: Uri?) {
        viewModelScope.launch {
            _milestonePhotoFourUri.value = uri
        }

    }



}
