package com.example.monitoringandevaluationapp.domain.usecases

import com.example.monitoringandevaluationapp.data.api.model.ApiResponse
import com.example.monitoringandevaluationapp.data.api.model.Enterprise
import com.example.monitoringandevaluationapp.data.repository.PostProjectEnterpriseRepository

class FetchAndPostEnterpriseUseCaseImpl(
    private val enterprise: Enterprise,
    private val postProjectEnterpriseRepository: PostProjectEnterpriseRepository
): FetchAndPostEnterpriseUseCase {
    override suspend fun execute(): ApiResponse {
        // Post the data to the server
        return if(enterprise != null) {
            postProjectEnterpriseRepository.saveEnterprise(enterprise)
        } else {
            // Handle the case when there is no data to post
            ApiResponse("No data to post")
        }
    }
}