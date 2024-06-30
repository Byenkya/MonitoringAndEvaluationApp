package com.example.monitoringandevaluationapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.monitoringandevaluationapp.data.LocationEntity
import com.example.monitoringandevaluationapp.data.PdmProjectAssessmentEntity
import com.example.monitoringandevaluationapp.data.ProjectAssessmentDao
import java.io.File

class PdmProjectAssessmentRepository(private val projectAssessmentDao: ProjectAssessmentDao) {
    suspend fun saveProjectAssessment(pdmProjectAssessmentEntity: PdmProjectAssessmentEntity) {
        projectAssessmentDao.insertAssessment(pdmProjectAssessmentEntity)
    }

    fun getAllAssessments(): LiveData<List<PdmProjectAssessmentEntity>> {
        return projectAssessmentDao.getAllProjectAssessment()
    }

    fun getAssessmentById(projectId: Long): LiveData<PdmProjectAssessmentEntity> {
        return projectAssessmentDao.getAssessmentById(projectId)
    }

    suspend fun markProjectAssessmentAsUploaded(projectAssessmentId: Long) {
        projectAssessmentDao.markProjectAssessmentAsUploaded(projectAssessmentId)
    }

    suspend fun deleteAssessment(pdmProjectAssessmentEntity: PdmProjectAssessmentEntity) {
        // delete images from the system
//        deleteImages(location.memberPhotoPath)
        deleteImages(pdmProjectAssessmentEntity.assessedPhotoOnePath)
        deleteImages(pdmProjectAssessmentEntity.assessedPhotoTwoPath)
        deleteImages(pdmProjectAssessmentEntity.assessedPhotoThreePath)
        deleteImages(pdmProjectAssessmentEntity.assessedPhotoFourPath)
        deleteImages(pdmProjectAssessmentEntity.milestonePhotoOnePath)
        deleteImages(pdmProjectAssessmentEntity.milestonePhotoTwoPath)
        deleteImages(pdmProjectAssessmentEntity.milestonePhotoThreePath)
        deleteImages(pdmProjectAssessmentEntity.milestonePhotoFourPath)

        // finally delete the location
        projectAssessmentDao.deleteAssessmentById(pdmProjectAssessmentEntity.id)
    }

    private fun deleteImages(vararg filePaths: String?) {
        filePaths.filterNotNull().forEach { filePath ->
            val file = File(filePath)
            if (file.exists()) {
                file.delete()
            }

        }
    }

}