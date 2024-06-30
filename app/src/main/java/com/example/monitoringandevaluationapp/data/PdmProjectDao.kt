package com.example.monitoringandevaluationapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PdmProjectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPdmProject(pdmProject: PdmProjectEntity)

    @Query("SELECT * FROM pdmProjectEntity")
    fun getAllPdmProjects(): LiveData<List<PdmProjectEntity>>

    @Query("SELECT * FROM pdmProjectEntity WHERE id = :projectID")
    fun getPdmProjectById(projectID: Long): LiveData<PdmProjectEntity>

    @Query("DELETE FROM pdmProjectEntity WHERE id = :projectID")
    suspend fun deletePdmProjectById(projectID: Long)

    @Query("UPDATE pdmProjectEntity SET uploaded = 1 WHERE id = :projectID")
    suspend fun markProjectAsUploaded(projectID: Long)
}