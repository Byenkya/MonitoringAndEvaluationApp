package com.example.monitoringandevaluationapp.domain.usecases

import com.example.monitoringandevaluationapp.data.api.model.ApiResponse
import com.example.monitoringandevaluationapp.data.api.model.PdmProject
import com.example.monitoringandevaluationapp.data.repository.PostProjectPdmRepository

class FetchAndPostPdmProjectUseCaseImpl(
    private val pdmProject: PdmProject,
    private val postProjectPdmRepository: PostProjectPdmRepository
): FetchAndPostPdmProjectUseCase {
    override suspend fun execute(): ApiResponse {
        return if(pdmProject != null) {
            postProjectPdmRepository.savePdmProject(pdmProject)
        } else {
            // Handle the case when there is no data to post
            ApiResponse("No data to post")
        }
    }
}