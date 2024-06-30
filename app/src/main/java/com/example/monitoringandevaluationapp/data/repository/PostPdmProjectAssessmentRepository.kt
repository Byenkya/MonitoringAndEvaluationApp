package com.example.monitoringandevaluationapp.data.repository

import com.example.monitoringandevaluationapp.data.ApiService
import com.example.monitoringandevaluationapp.data.api.model.ApiResponse
import com.example.monitoringandevaluationapp.data.api.model.PdmProjectAssessment
import com.example.monitoringandevaluationapp.data.api.model.Project
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class PostPdmProjectAssessmentRepository(private val apiService: ApiService) {
    suspend fun saveProjectAssessment(pdmProjectAssessment: PdmProjectAssessment): ApiResponse {
        try {
            val images = mutableListOf<MultipartBody.Part>()
            val assessmentPhoto1 = File(pdmProjectAssessment.assessed_photo1)
            val assessmentPhoto2 = File(pdmProjectAssessment.assessed_photo2)
            val assessmentPhoto3 = File(pdmProjectAssessment.assessed_photo4)
            val assessmentPhoto4 = File(pdmProjectAssessment.assessed_photo4)
            val milestonePhoto1 = File(pdmProjectAssessment.milestone_photo1)
            val milestonePhoto2 = File(pdmProjectAssessment.milestone_photo2)
            val milestonePhoto3 = File(pdmProjectAssessment.milestone_photo3)
            val milestonePhoto4 = File(pdmProjectAssessment.milestone_photo4)

            val requestBody1 = RequestBody.create("image/*".toMediaTypeOrNull(), assessmentPhoto1)
            val imagePart1 = MultipartBody.Part.createFormData("images", assessmentPhoto1.name, requestBody1)
            val requestBody2 = RequestBody.create("image/*".toMediaTypeOrNull(), assessmentPhoto2)
            val imagePart2 = MultipartBody.Part.createFormData("images", assessmentPhoto2.name, requestBody2)
            val requestBody3 = RequestBody.create("image/*".toMediaTypeOrNull(), assessmentPhoto3)
            val imagePart3 = MultipartBody.Part.createFormData("images", assessmentPhoto3.name, requestBody3)
            val requestBody4 = RequestBody.create("image/*".toMediaTypeOrNull(), assessmentPhoto4)
            val imagePart4 = MultipartBody.Part.createFormData("images", assessmentPhoto4.name, requestBody4)
            val requestBody5 = RequestBody.create("image/*".toMediaTypeOrNull(), milestonePhoto1)
            val imagePart5 = MultipartBody.Part.createFormData("images", milestonePhoto1.name, requestBody5)
            val requestBody6 = RequestBody.create("image/*".toMediaTypeOrNull(), milestonePhoto2)
            val imagePart6 = MultipartBody.Part.createFormData("images", milestonePhoto2.name, requestBody6)
            val requestBody7 = RequestBody.create("image/*".toMediaTypeOrNull(), milestonePhoto3)
            val imagePart7 = MultipartBody.Part.createFormData("images", milestonePhoto3.name, requestBody7)
            val requestBody8 = RequestBody.create("image/*".toMediaTypeOrNull(), milestonePhoto4)
            val imagePart8 = MultipartBody.Part.createFormData("images", milestonePhoto4.name, requestBody8)

            images.add(imagePart1)
            images.add(imagePart2)
            images.add(imagePart3)
            images.add(imagePart4)
            images.add(imagePart5)
            images.add(imagePart6)
            images.add(imagePart7)
            images.add(imagePart8)

            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            val projectAssessmentJsonAdapter = moshi.adapter(PdmProjectAssessment::class.java)
            val projectAssessmentJson = projectAssessmentJsonAdapter.toJson(pdmProjectAssessment)

            val projectAssessmentRequestBody = RequestBody.create("application/json".toMediaTypeOrNull(), projectAssessmentJson)
            val projectAssessmentPart = MultipartBody.Part.createFormData("project_assessment", null, projectAssessmentRequestBody)

            // Make the API call
            return apiService.saveProjectAssessment(projectAssessmentPart, images)
        } catch (e: Exception) {
            return ApiResponse("Error: ${e.message}")
        }
    }
}