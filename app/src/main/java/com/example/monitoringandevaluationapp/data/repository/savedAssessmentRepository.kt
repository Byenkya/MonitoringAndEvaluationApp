package com.example.monitoringandevaluationapp.data.repository

import com.example.monitoringandevaluationapp.data.SavedAssessmentDao
import com.example.monitoringandevaluationapp.data.SavedAssessmentEntity

class savedAssessmentRepository(private val savedAssessmentDao: SavedAssessmentDao) {
    suspend fun savedAssessmentID(projectAssessment: SavedAssessmentEntity) {
        savedAssessmentDao.insertProjectAssessment(projectAssessment)
    }

    suspend fun getSavedAssessmentById(projectId: String): SavedAssessmentEntity?{
        return savedAssessmentDao.getSavedAssessmentById(projectId)
    }
}