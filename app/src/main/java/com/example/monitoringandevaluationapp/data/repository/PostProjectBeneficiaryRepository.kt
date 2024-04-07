package com.example.monitoringandevaluationapp.data.repository

import com.example.monitoringandevaluationapp.data.ApiService
import com.example.monitoringandevaluationapp.data.api.model.ApiResponse
import com.example.monitoringandevaluationapp.data.api.model.Beneficiary
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody

class PostProjectBeneficiaryRepository(private val apiService: ApiService) {
    suspend fun saveBeneficiary(beneficiary: Beneficiary): ApiResponse {
        try{
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            val beneficiaryJsonAdapter = moshi.adapter(Beneficiary::class.java)
            val beneficiaryJson = beneficiaryJsonAdapter.toJson(beneficiary)

            val beneficiaryRequestBody = RequestBody.create("application/json".toMediaTypeOrNull(), beneficiaryJson)
            val beneficiaryPart = MultipartBody.Part.createFormData("beneficiary", null, beneficiaryRequestBody)

            // Make the API call
            return apiService.saveBeneficiary(beneficiaryPart)
        } catch (e: Exception) {
            return ApiResponse("Error: ${e.message}")
        }


    }

}