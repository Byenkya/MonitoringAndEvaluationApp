package com.example.monitoringandevaluationapp.data.api.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class Project(
    @Json(name = "id")
    val id: String,
    @Json(name = "groupName")
    val groupName: String,
    @Json(name = "groupDescription")
    val groupDescription: String,
    @Json(name = "foundingDate")
    val foundingDate: String,
    @Json(name = "registered")
    val registered: Boolean,
    @Json(name = "registrationNumber")
    val registrationNumber: Int,
    @Json(name = "registrationDate")
    val registrationDate: String,
    @Json(name = "village")
    val village: String,
    @Json(name = "parish")
    val parish: String,
    @Json(name = "subCounty")
    val subCounty: String,
    @Json(name = "county")
    val county: String,
    @Json(name = "district")
    val district: String,
    @Json(name = "subRegion")
    val subRegion: String,
    @Json(name = "country")
    val country: String,
    @Json(name = "createdBy")
    val createdBy: String,
    @Json(name = "createdOn")
    val createdOn: String,
    @Json(name = "uuid")
    val uuid: String,
    @Json(name = "memberShipNumber")
    val memberShipNumber: Long,
    @Json(name = "firstName")
    val firstName: String,
    @Json(name = "lastName")
    val lastName: String,
    @Json(name = "otherName")
    val otherName: String,
    @Json(name = "gender")
    val gender: String,
    @Json(name = "dob")
    val dob: String,
    @Json(name = "paid")
    val paid: Boolean,
    @Json(name = "memberRole")
    val memberRole: String,
    @Json(name = "memberPhotoPath")
    val memberPhotoPath: String,
    @Json(name = "otherDetails")
    val otherDetails: String,
    @Json(name = "projectNumber")
    val projectNumber: Long,
    @Json(name = "projectName")
    val projectName: String,
    @Json(name = "projectFocus")
    val projectFocus: String,
    @Json(name = "startDate")
    val startDate: String,
    @Json(name = "endDate")
    val endDate: String,
    @Json(name = "expectedDate")
    val expectedDate: String,
    @Json(name = "fundedBy")
    val fundedBy: String,
    @Json(name = "amount")
    val amount: Long,
    @Json(name = "teamLeader")
    val teamLeader: String,
    @Json(name = "teamLeaderEmail")
    val teamLeaderEmail: String,
    @Json(name = "teamLeaderPhone")
    val teamLeaderPhone: String,
    @Json(name = "otherProjectContacts")
    val otherProjectContacts: String,
    @Json(name = "projectDescription")
    val projectDescription: String,
    @Json(name = "assessmentDate")
    val assessmentDate: String,
    @Json(name = "assessMilestone")
    val assessMilestone: Boolean,
    @Json(name = "assessedBy")
    val assessedBy: String,
    @Json(name = "assessmentFor")
    val assessmentFor: String,
    @Json(name = "observation")
    val observation: String,
    @Json(name = "photoOnePath")
    val photoOnePath: String,
    @Json(name = "photoTwoPath")
    val photoTwoPath: String,
    @Json(name = "photoThreePath")
    val photoThreePath: String,
    @Json(name = "photoFourPath")
    val photoFourPath: String,
    @Json(name = "latitude")
    val latitude: Double,
    @Json(name = "longitude")
    val longitude: Double,
    @Json(name = "altitude")
    val altitude: String,
    @Json(name = "gpsCrs")
    val gpsCrs: String,
    @Json(name = "milestoneDate")
    val milestoneDate: String,
    @Json(name = "milestoneDetails")
    val milestoneDetails: String,
    @Json(name = "milestoneTarget")
    val milestoneTarget: String,
    @Json(name = "milestoneTargetDate")
    val milestoneTargetDate: String,
    @Json(name = "assignedTo")
    val assignedTo: String,
    @Json(name = "status")
    val status: String,
    @Json(name = "mileStoneComments")
    val mileStoneComments: String,
    @Json(name = "milestonePhotoOnePath")
    val milestonePhotoOnePath: String,
    @Json(name = "milestonePhotoTwoPath")
    val milestonePhotoTwoPath: String,
    @Json(name = "milestonePhotoThreePath")
    val milestonePhotoThreePath: String,
    @Json(name = "milestonePhotoFourPath")
    val milestonePhotoFourPath: String,
)