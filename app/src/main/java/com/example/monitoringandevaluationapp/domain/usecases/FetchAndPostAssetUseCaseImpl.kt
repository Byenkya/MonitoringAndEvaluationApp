package com.example.monitoringandevaluationapp.domain.usecases

import com.example.monitoringandevaluationapp.data.LocationEntity
import com.example.monitoringandevaluationapp.data.api.model.ApiResponse
import com.example.monitoringandevaluationapp.data.api.model.Asset
import com.example.monitoringandevaluationapp.data.api.model.Project
import com.example.monitoringandevaluationapp.data.repository.PostProjectAssetRepository
import com.example.monitoringandevaluationapp.presentation.ViewModel.PDMViewModel

class FetchAndPostAssetUseCaseImpl(
    private val asset: Asset,
    private val postProjectAssetRepository: PostProjectAssetRepository
): FetchAndPostAssetUseCase {
    override suspend fun execute(): ApiResponse {

        // Post the data to the server
        return if (asset != null) {
            postProjectAssetRepository.saveAsset(asset)
        } else {
            // Handle the case when there is no data to post
            ApiResponse("No data to post")
        }
    }


}