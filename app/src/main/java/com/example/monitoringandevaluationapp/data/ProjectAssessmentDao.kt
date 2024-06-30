package com.example.monitoringandevaluationapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProjectAssessmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssessment(assessment: PdmProjectAssessmentEntity)

    @Query("SELECT * FROM PdmProjectAssessmentEntity")
    fun getAllProjectAssessment(): LiveData<List<PdmProjectAssessmentEntity>>

    @Query("SELECT * FROM PdmProjectAssessmentEntity WHERE id = :projectId")
    fun getAssessmentById(projectId: Long): LiveData<PdmProjectAssessmentEntity>

    @Query("DELETE FROM PdmProjectAssessmentEntity WHERE id = :projectId")
    suspend fun deleteAssessmentById(projectId: Long)

    @Query("UPDATE PdmProjectAssessmentEntity SET uploaded = 1 WHERE id = :projectAssessmentID")
    suspend fun markProjectAssessmentAsUploaded(projectAssessmentID: Long)
}