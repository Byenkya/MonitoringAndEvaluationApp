package com.example.monitoringandevaluationapp.domain.usecases

import com.example.monitoringandevaluationapp.data.api.model.ApiResponse
import com.example.monitoringandevaluationapp.data.api.model.Group
import com.example.monitoringandevaluationapp.data.repository.PostProjectGroupRepository

class FetchAndPostGroupUseCaseImpl(
    private val group: Group,
    private val postProjectGroupRepository: PostProjectGroupRepository
): FetchAndPostGroupUseCase {
    override suspend fun execute(): ApiResponse {
        // Post the data to the server
        return if(group != null) {
            postProjectGroupRepository.saveGroup(group)
        } else {
            // Handle the case when there is no data to post
            ApiResponse("No data to post")
        }
    }
}