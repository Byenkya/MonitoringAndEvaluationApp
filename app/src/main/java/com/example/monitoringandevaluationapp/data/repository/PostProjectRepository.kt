package com.example.monitoringandevaluationapp.data.repository

import android.util.Log
import com.example.monitoringandevaluationapp.data.ApiService
import com.example.monitoringandevaluationapp.data.api.model.ApiResponse
import com.example.monitoringandevaluationapp.data.api.model.Project
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class PostProjectRepository(private val apiService: ApiService) {
    suspend fun saveProjects(projects: List<Project>): ApiResponse {
        val images = mutableListOf<MultipartBody.Part>()

        // Iterate through each project and add its associated image to the request
        for (project in projects) {
            val imageFile = File(project.milestonePhotoThreePath) // Adjust the file path variable as needed
            if (imageFile.exists()) {
                val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), imageFile)
                val imagePart = MultipartBody.Part.createFormData("images[]", imageFile.name, requestBody)
                images.add(imagePart)
            }
        }

        // Make the API call
        return apiService.saveProjects(projects, images)
    }

    suspend fun saveProject(project: Project): ApiResponse {
        try {
            val images = mutableListOf<MultipartBody.Part>()
            val memberPhotoFile = File(project.memberPhotoPath)
            val photoOneFile = File(project.photoOnePath)
            val photoTwoFile = File(project.photoTwoPath)
            val photoThreeFile = File(project.photoThreePath)
            val photoFourPath = File(project.photoFourPath)
            val milestonePhotoOneFile = File(project.milestonePhotoOnePath)
            val milestonePhotoTwoFile = File(project.milestonePhotoTwoPath)
            val milestonePhotoThreeFile = File(project.milestonePhotoThreePath)
            val milestonePhotoFourFile = File(project.milestonePhotoFourPath)
            val requestBody1 = RequestBody.create("image/*".toMediaTypeOrNull(), memberPhotoFile)
            val imagePart1 = MultipartBody.Part.createFormData("images", memberPhotoFile.name, requestBody1)
            val requestBody2 = RequestBody.create("image/*".toMediaTypeOrNull(), photoOneFile)
            val imagePart2 = MultipartBody.Part.createFormData("images", photoOneFile.name, requestBody2)
            val requestBody3 = RequestBody.create("image/*".toMediaTypeOrNull(), photoTwoFile)
            val imagePart3 = MultipartBody.Part.createFormData("images", photoTwoFile.name, requestBody3)
            val requestBody4 = RequestBody.create("image/*".toMediaTypeOrNull(), photoThreeFile)
            val imagePart4 = MultipartBody.Part.createFormData("images", photoThreeFile.name, requestBody4)
            val requestBody5 = RequestBody.create("image/*".toMediaTypeOrNull(), photoFourPath)
            val imagePart5 = MultipartBody.Part.createFormData("images", photoFourPath.name, requestBody5)
            val requestBody6 = RequestBody.create("image/*".toMediaTypeOrNull(), milestonePhotoOneFile)
            val imagePart6 = MultipartBody.Part.createFormData("images", milestonePhotoOneFile.name, requestBody6)
            val requestBody7 = RequestBody.create("image/*".toMediaTypeOrNull(), milestonePhotoTwoFile)
            val imagePart7 = MultipartBody.Part.createFormData("images", milestonePhotoTwoFile.name, requestBody7)
            val requestBody8 = RequestBody.create("image/*".toMediaTypeOrNull(), milestonePhotoThreeFile)
            val imagePart8 = MultipartBody.Part.createFormData("images", milestonePhotoThreeFile.name, requestBody8)
            val requestBody9 = RequestBody.create("image/*".toMediaTypeOrNull(), milestonePhotoFourFile)
            val imagePart9 = MultipartBody.Part.createFormData("images", milestonePhotoFourFile.name, requestBody9)
            images.add(imagePart1)
            images.add(imagePart2)
            images.add(imagePart3)
            images.add(imagePart4)
            images.add(imagePart5)
            images.add(imagePart6)
            images.add(imagePart7)
            images.add(imagePart8)
            images.add(imagePart9)

            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            val projectJsonAdapter = moshi.adapter(Project::class.java)
            val projectJson = projectJsonAdapter.toJson(project)

            val projectRequestBody = RequestBody.create("application/json".toMediaTypeOrNull(), projectJson)
            val projectPart = MultipartBody.Part.createFormData("project", null, projectRequestBody)

            // Make the API call
            return apiService.saveProject(projectPart, images)
        } catch (e: Exception) {
            return ApiResponse("Error: ${e.message}")
        }
    }
}
