package com.example.monitoringandevaluationapp.domain.usecases

import com.example.monitoringandevaluationapp.data.PdmProjectAssessmentEntity
import com.example.monitoringandevaluationapp.data.api.model.ApiResponse
import com.example.monitoringandevaluationapp.data.api.model.PdmProjectAssessment
import com.example.monitoringandevaluationapp.data.repository.PostPdmProjectAssessmentRepository

class FetchAndPostPdmProjectAssessmentUseCaseImpl(
    private val pdmProjectAssessmentEntity: PdmProjectAssessmentEntity,
    private val postPdmProjectAssessmentRepository: PostPdmProjectAssessmentRepository
) : FetchAndPostPdmProjectAssessmentUseCase {
    override suspend fun execute(): ApiResponse {
        // Map projectAssessment Entity to PdmProjectAssessment
        val projectAssessment = mapPdmProjectAssessmentToPdmProjectAssessment(pdmProjectAssessmentEntity)

        // Post the data to the server
        return if (projectAssessment != null) {
            postPdmProjectAssessmentRepository.saveProjectAssessment(projectAssessment)
        } else {
            // Handle the case when there is no data to post
            ApiResponse("No data to post")
        }
    }

    private fun mapPdmProjectAssessmentToPdmProjectAssessment(
        pdmProjectAssessmentEntity: PdmProjectAssessmentEntity
    ): PdmProjectAssessment {
        return PdmProjectAssessment(
            geom = pdmProjectAssessmentEntity.geom,
            uuid = pdmProjectAssessmentEntity.uuid,
            group_name = pdmProjectAssessmentEntity.groupName,
            group_id = pdmProjectAssessmentEntity.groupId,
            lat_x = pdmProjectAssessmentEntity.longitude,
            lon_y = pdmProjectAssessmentEntity.lat,
            assessed_by = pdmProjectAssessmentEntity.assessedBy,
            date_assessed = pdmProjectAssessmentEntity.dateAssessed,
            assess_milestones = pdmProjectAssessmentEntity.assessMilestones,
            assessed_for = pdmProjectAssessmentEntity.assessedFor,
            observation = pdmProjectAssessmentEntity.observation,
            assessed_photo1 = pdmProjectAssessmentEntity.assessedPhotoOnePath,
            assessed_photo2 = pdmProjectAssessmentEntity.assessedPhotoTwoPath,
            assessed_photo3 = pdmProjectAssessmentEntity.assessedPhotoThreePath,
            assessed_photo4 = pdmProjectAssessmentEntity.assessedPhotoFourPath,
            milestone_detail = pdmProjectAssessmentEntity.milestoneDetails,
            milestone_target = pdmProjectAssessmentEntity.milestoneTarget,
            milestone_target_date = pdmProjectAssessmentEntity.milestoneTargetDate,
            assigned_to = pdmProjectAssessmentEntity.assignedTo,
            milestone_status = pdmProjectAssessmentEntity.milestoneStatus,
            milestone_photo1 = pdmProjectAssessmentEntity.milestonePhotoOnePath,
            milestone_photo2 = pdmProjectAssessmentEntity.milestonePhotoTwoPath,
            milestone_photo3 = pdmProjectAssessmentEntity.milestonePhotoThreePath,
            milestone_photo4 = pdmProjectAssessmentEntity.milestonePhotoFourPath,
            updated_by = pdmProjectAssessmentEntity.updatedBy,
            date_updated = pdmProjectAssessmentEntity.dateUpdated,
            project_name = pdmProjectAssessmentEntity.projectName,
            project_id = pdmProjectAssessmentEntity.projectID
        )
    }
}