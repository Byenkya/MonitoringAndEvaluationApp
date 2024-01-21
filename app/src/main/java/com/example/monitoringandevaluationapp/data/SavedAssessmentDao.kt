package com.example.monitoringandevaluationapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
@Dao
interface SavedAssessmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProjectAssessment(savedAssessmentEntity: SavedAssessmentEntity)

    @Query("SELECT * FROM SavedAssessmentEntity WHERE savedAssessmentId = :savedAssessmentId")
    suspend fun getSavedAssessmentById(savedAssessmentId: String): SavedAssessmentEntity?
}