package com.example.monitoringandevaluationapp.data.repository

import com.example.monitoringandevaluationapp.data.ApiService
import com.example.monitoringandevaluationapp.data.api.model.ApiResponse
import com.example.monitoringandevaluationapp.data.api.model.Enterprise
import com.example.monitoringandevaluationapp.data.api.model.PdmProject
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody

class PostProjectPdmRepository(private val apiService: ApiService) {

    suspend fun savePdmProject(pdmProject: PdmProject): ApiResponse {
        try {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            val pdmProjectJsonAdapter = moshi.adapter(PdmProject::class.java)
            val pdmProjectJson = pdmProjectJsonAdapter.toJson(pdmProject)

            val pdmProjectRequestBody = RequestBody.create("application/json".toMediaTypeOrNull(), pdmProjectJson)
            val pdmProjectPart = MultipartBody.Part.createFormData("project", null, pdmProjectRequestBody)

            // Make the API call
            return apiService.savePdmProject(pdmProjectPart)

        } catch (e: Exception) {
            return ApiResponse("Error: ${e.message}")
        }
    }
}