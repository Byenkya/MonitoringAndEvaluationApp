package com.example.monitoringandevaluationapp.domain.usecases

import com.example.monitoringandevaluationapp.data.api.model.ApiResponse

interface FetchAndPostProjectUseCase {
    suspend fun execute(): ApiResponse
}