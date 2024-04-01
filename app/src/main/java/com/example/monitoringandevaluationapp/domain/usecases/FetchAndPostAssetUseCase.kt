package com.example.monitoringandevaluationapp.domain.usecases

import com.example.monitoringandevaluationapp.data.api.model.ApiResponse

interface FetchAndPostAssetUseCase {
    suspend fun execute(): ApiResponse
}