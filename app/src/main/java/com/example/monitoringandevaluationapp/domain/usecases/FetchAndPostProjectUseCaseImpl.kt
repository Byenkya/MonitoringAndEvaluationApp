package com.example.monitoringandevaluationapp.domain.usecases

import com.example.monitoringandevaluationapp.data.LocationEntity
import com.example.monitoringandevaluationapp.data.api.model.ApiResponse
import com.example.monitoringandevaluationapp.data.api.model.Project
import com.example.monitoringandevaluationapp.data.repository.PostProjectRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

class FetchAndPostProjectUseCaseImpl(
    private val locationEntity: LocationEntity,
    private val postProjectRepository: PostProjectRepository
) : FetchAndPostProjectUseCase{
    override suspend fun execute(): ApiResponse {

        // Assuming you have a method to map LocationEntity to Project
        val project = mapLocationEntityToProject(locationEntity)

        // Post the data to the server
        return if (project != null) {
            postProjectRepository.saveProject(project)
        } else {
            // Handle the case when there is no data to post
            ApiResponse("No data to post")
        }
    }

    private fun mapLocationEntityToProject(locationEntity: LocationEntity): Project {
        // Implement the mapping logic based on your requirements
        // This is just a placeholder
        return Project(
            id = locationEntity.id,
            groupName = locationEntity.groupName,
            groupDescription = locationEntity.groupDescription,
            foundingDate = locationEntity.foundingDate,
            registered = locationEntity.registered,
            registrationNumber = locationEntity.registrationNumber,
            registrationDate = locationEntity.registrationDate,
            village = locationEntity.village,
            parish = locationEntity.parish,
            subCounty = locationEntity.subCounty,
            county = locationEntity.county,
            district = locationEntity.district,
            subRegion = locationEntity.subRegion,
            country = locationEntity.country,
            createdBy = locationEntity.createdBy,
            createdOn = locationEntity.createdOn,
            uuid = locationEntity.uuid,
            memberShipNumber = locationEntity.memberShipNumber,
            firstName = locationEntity.firstName,
            lastName = locationEntity.lastName,
            otherName = locationEntity.otherName,
            gender = locationEntity.gender,
            dob = locationEntity.dob,
            paid = locationEntity.paid,
            memberRole = locationEntity.memberRole,
            memberPhotoPath = locationEntity.memberPhotoPath,
            otherDetails = locationEntity.otherDetails,
            projectNumber = locationEntity.projectNumber,
            projectName = locationEntity.projectName,
            projectFocus = locationEntity.projectFocus,
            startDate = locationEntity.startDate,
            endDate = locationEntity.endDate,
            expectedDate = locationEntity.expectedDate,
            fundedBy = locationEntity.fundedBy,
            amount = locationEntity.amount,
            teamLeader = locationEntity.teamLeader,
            teamLeaderEmail = locationEntity.teamLeaderEmail,
            teamLeaderPhone = locationEntity.teamLeaderPhone,
            otherProjectContacts = locationEntity.otherProjectContacts,
            projectDescription = locationEntity.projectDescription,
            assessmentDate = locationEntity.assessmentDate,
            assessMilestone = locationEntity.assessMilestone,
            assessedBy = locationEntity.assessedBy,
            assessmentFor = locationEntity.assessmentFor,
            observation = locationEntity.observation,
            photoOnePath = locationEntity.photoOnePath,
            photoTwoPath = locationEntity.photoTwoPath,
            photoThreePath = locationEntity.photoThreePath,
            photoFourPath = locationEntity.photoFourPath,
            latitude = locationEntity.latitude,
            longitude = locationEntity.longitude,
            altitude = locationEntity.altitude,
            gpsCrs = locationEntity.gpsCrs,
            milestoneDate = locationEntity.milestoneDate,
            milestoneDetails = locationEntity.milestoneDetails,
            milestoneTarget = locationEntity.milestoneTarget,
            milestoneTargetDate = locationEntity.milestoneTargetDate,
            assignedTo = locationEntity.assignedTo,
            status = locationEntity.status,
            mileStoneComments = locationEntity.mileStoneComments,
            milestonePhotoOnePath = locationEntity.milestonePhotoOnePath,
            milestonePhotoTwoPath = locationEntity.milestonePhotoTwoPath,
            milestonePhotoThreePath = locationEntity.milestonePhotoThreePath,
            milestonePhotoFourPath = locationEntity.milestonePhotoFourPath
        )
    }
}