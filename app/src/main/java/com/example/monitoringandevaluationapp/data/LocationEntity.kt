package com.example.monitoringandevaluationapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val latitude: Double,
    val longitude: Double,
    val description: String,
    val imagePath: String
)

