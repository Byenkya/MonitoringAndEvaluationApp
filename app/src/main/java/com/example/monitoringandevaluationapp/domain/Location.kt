package com.example.monitoringandevaluationapp.domain

data class Location(
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val description: String,
    val imagePath: String
)
