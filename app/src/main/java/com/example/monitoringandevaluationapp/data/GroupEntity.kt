package com.example.monitoringandevaluationapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GroupEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    val descr: String
)
