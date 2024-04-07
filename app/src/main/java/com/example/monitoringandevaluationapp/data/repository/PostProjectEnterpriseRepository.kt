package com.example.monitoringandevaluationapp.data.repository

import com.example.monitoringandevaluationapp.data.ApiService
import com.example.monitoringandevaluationapp.data.api.model.ApiResponse
import com.example.monitoringandevaluationapp.data.api.model.Enterprise
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody

class PostProjectEnterpriseRepository(private val apiService: ApiService) {
    suspend fun saveEnterprise(enterprise: Enterprise): ApiResponse {
        try {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            val enterpriseJsonAdapter = moshi.adapter(Enterprise::class.java)
            val enterpriseJson = enterpriseJsonAdapter.toJson(enterprise)

            val enterpriseRequestBody = RequestBody.create("application/json".toMediaTypeOrNull(), enterpriseJson)
            val enterprisePart = MultipartBody.Part.createFormData("enterprise", null, enterpriseRequestBody)

            // Make the API call
            return apiService.saveEnterprise(enterprisePart)

        } catch (e: Exception) {
            return ApiResponse("Error: ${e.message}")
        }
    }
}