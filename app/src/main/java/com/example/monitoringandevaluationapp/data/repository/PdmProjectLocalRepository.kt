package com.example.monitoringandevaluationapp.data.repository

import androidx.lifecycle.LiveData
import com.example.monitoringandevaluationapp.data.GroupEntity
import com.example.monitoringandevaluationapp.data.PdmProjectDao
import com.example.monitoringandevaluationapp.data.PdmProjectEntity

class PdmProjectLocalRepository(private val pdmProjectDao: PdmProjectDao) {
    suspend fun savePdmProject(pdmProjectEntity: PdmProjectEntity) {
        pdmProjectDao.insertPdmProject(pdmProjectEntity)
    }

    fun getAllPdmProjects(): LiveData<List<PdmProjectEntity>> {
        return pdmProjectDao.getAllPdmProjects()
    }

    fun getPdmProjectById(groupId: Long): LiveData<PdmProjectEntity> {
        return pdmProjectDao.getPdmProjectById(groupId)
    }

    suspend fun markProjectUploaded(projectId: Long) {
        pdmProjectDao.markProjectAsUploaded(projectId)
    }

    suspend fun deletePdmProject(pdmProjectEntity: PdmProjectEntity) {
        // finally delete the location
        pdmProjectDao.deletePdmProjectById(pdmProjectEntity.id)
    }

}