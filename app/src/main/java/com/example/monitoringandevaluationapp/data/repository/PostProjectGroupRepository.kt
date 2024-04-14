package com.example.monitoringandevaluationapp.data.repository

import com.example.monitoringandevaluationapp.data.ApiService
import com.example.monitoringandevaluationapp.data.api.model.ApiResponse
import com.example.monitoringandevaluationapp.data.api.model.Enterprise
import com.example.monitoringandevaluationapp.data.api.model.Group
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody

class PostProjectGroupRepository(private val apiService: ApiService) {
    suspend fun saveGroup(group: Group): ApiResponse {
        try {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            val groupJsonAdapter = moshi.adapter(Group::class.java)
            val groupJson = groupJsonAdapter.toJson(group)

            val groupRequestBody = RequestBody.create("application/json".toMediaTypeOrNull(), groupJson)
            val groupPart = MultipartBody.Part.createFormData("group", null, groupRequestBody)

            // Make the API call
            return apiService.saveGroup(groupPart)


        } catch (e: Exception) {
            return ApiResponse("Error: ${e.message}")
        }
    }
}