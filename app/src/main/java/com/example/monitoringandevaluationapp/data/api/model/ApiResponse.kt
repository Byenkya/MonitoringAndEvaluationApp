package com.example.monitoringandevaluationapp.data.api.model

import com.squareup.moshi.Json

data class ApiResponse(
    @Json(name = "message")
    val message: String
)
