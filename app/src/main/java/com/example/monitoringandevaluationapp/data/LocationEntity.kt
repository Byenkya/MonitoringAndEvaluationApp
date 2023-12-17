package com.example.monitoringandevaluationapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val groupName: String,
    val groupDescription: String,
    val foundingDate: String,
    val registered: Boolean,
    val registrationNumber: Int,
    val registrationDate: String,
    val village: String,
    val parish: String,
    val subCounty: String,
    val county: String,
    val district: String,
    val subRegion: String,
    val country: String,
    val createdBy: String,
    val createdOn: String,
    val uuid: String,
    val memberShipNumber: Long,
    val firstName: String,
    val lastName: String,
    val otherName: String,
    val gender: String,
    val dob: String,
    val paid: Boolean,
    val memberRole: String,
    val memberPhotoPath: String,
    val otherDetails: String,
    val projectNumber: Long,
    val projectName: String,
    val projectFocus: String,
    val startDate: String,
    val endDate: String,
    val expectedDate: String,
    val fundedBy: String,
    val amount: Long ,
    val teamLeader: String,
    val teamLeaderEmail: String,
    val teamLeaderPhone: String,
    val otherProjectContacts: String,
    val projectDescription: String,
    val assessmentDate: String,
    val assessMilestone: Boolean,
    val assessedBy: String,
    val assessmentFor: String,
    val observation: String,
    val photoOnePath: String,
    val photoTwoPath: String,
    val photoThreePath: String,
    val photoFourPath: String,
    val latitude: Double,
    val longitude: Double,
    val altitude: String,
    val gpsCrs: String,
    val milestoneDate: String,
    val milestoneDetails: String,
    val milestoneTarget: String,
    val milestoneTargetDate: String,
    val assignedTo: String,
    val status: String,
    val mileStoneComments: String,
    val milestonePhotoOnePath: String,
    val milestonePhotoTwoPath: String,
    val milestonePhotoThreePath: String,
    val milestonePhotoFourPath: String
)

