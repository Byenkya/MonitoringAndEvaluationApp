package com.example.monitoringandevaluationapp.domain.usecases

import com.example.monitoringandevaluationapp.data.api.model.ApiResponse

interface FetchAndPostEnterpriseUseCase {
    suspend fun execute(): ApiResponse
}