package com.example.monitoringandevaluationapp.domain.usecases

import com.example.monitoringandevaluationapp.data.api.model.ApiResponse

interface FetchAndPostBeneficiaryUseCase {
    suspend fun execute(): ApiResponse
}