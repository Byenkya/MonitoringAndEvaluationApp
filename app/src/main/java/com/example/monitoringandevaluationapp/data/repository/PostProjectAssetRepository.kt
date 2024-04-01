package com.example.monitoringandevaluationapp.data.repository

import com.example.monitoringandevaluationapp.data.ApiService
import com.example.monitoringandevaluationapp.data.api.model.ApiResponse
import com.example.monitoringandevaluationapp.data.api.model.Asset
import com.example.monitoringandevaluationapp.data.api.model.Project
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class PostProjectAssetRepository(private val apiService: ApiService) {
    suspend fun saveAsset(asset: Asset): ApiResponse {
        try {
            val images = mutableListOf<MultipartBody.Part>()
            val assetPhoto1 = File(asset.asset_photo1)
            val assetPhoto2 = File(asset.asset_photo2)
            val requestBody1 = RequestBody.create("image/*".toMediaTypeOrNull(), assetPhoto1)
            val imagePart1 = MultipartBody.Part.createFormData("images", assetPhoto1.name, requestBody1)
            val requestBody2 = RequestBody.create("image/*".toMediaTypeOrNull(), assetPhoto2)
            val imagePart2 = MultipartBody.Part.createFormData("images", assetPhoto2.name, requestBody2)
            images.add(imagePart1)
            images.add(imagePart2)

            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            val assetJsonAdapter = moshi.adapter(Asset::class.java)
            val assetJson = assetJsonAdapter.toJson(asset)

            val assetRequestBody = RequestBody.create("application/json".toMediaTypeOrNull(), assetJson)
            val assetPart = MultipartBody.Part.createFormData("asset", null, assetRequestBody)

            // Make the API call
            return apiService.saveAsset(assetPart, images)

        } catch (e: Exception) {
            return ApiResponse("Error: ${e.message}")
        }
    }
}