package com.example.monitoringandevaluationapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SavedAssessmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val savedAssessmentId: String,
    val postedToServer: Boolean
)