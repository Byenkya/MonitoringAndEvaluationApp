package com.example.monitoringandevaluationapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PdmProjectAssessmentEntity")
data class PdmProjectAssessmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val geom: String,
    val uuid: String,
    val groupName: String,
    val groupId: Long,
    val lat: Double,
    val longitude: Double,
    val assessedBy: String,
    val dateAssessed: String,
    val assessMilestones: String,
    val assessedFor: String,
    val observation: String,
    val assessedPhotoOnePath: String,
    val assessedPhotoTwoPath: String,
    val assessedPhotoThreePath: String,
    val assessedPhotoFourPath: String,
    val milestoneDetails: String,
    val milestoneTarget: String,
    val milestoneTargetDate: String,
    val assignedTo: String,
    val milestoneStatus: String,
    val milestonePhotoOnePath: String,
    val milestonePhotoTwoPath: String,
    val milestonePhotoThreePath: String,
    val milestonePhotoFourPath: String,
    val updatedBy: String,
    val dateUpdated: String,
    val projectName: String,
    val projectID: Long,
    val uploaded: Boolean
)
