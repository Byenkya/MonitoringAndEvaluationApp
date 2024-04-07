package com.example.monitoringandevaluationapp.domain.usecases

import com.example.monitoringandevaluationapp.data.api.model.ApiResponse
import com.example.monitoringandevaluationapp.data.api.model.Beneficiary
import com.example.monitoringandevaluationapp.data.repository.PostProjectBeneficiaryRepository

class FetchAndPostBeneficiaryUseCaseImpl(
    private val beneficiary: Beneficiary,
    private val postProjectBeneficiaryRepository: PostProjectBeneficiaryRepository
): FetchAndPostBeneficiaryUseCase {
    override suspend fun execute(): ApiResponse {
        // Post the data to the server
        return if(beneficiary != null) {
            postProjectBeneficiaryRepository.saveBeneficiary(beneficiary)
        } else {
            // Handle the case when there is no data to post
            ApiResponse("No data to post")
        }
    }
}