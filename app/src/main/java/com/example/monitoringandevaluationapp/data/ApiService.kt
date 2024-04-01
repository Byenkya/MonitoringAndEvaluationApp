package com.example.monitoringandevaluationapp.data

import com.example.monitoringandevaluationapp.data.api.model.ApiResponse
import com.example.monitoringandevaluationapp.data.api.model.Project
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @GET("/posts")
    suspend fun getPosts(): List<Project>

    @POST("/project")
    @Multipart
    suspend fun saveProject(
        @Part project: MultipartBody.Part,
        @Part images: List<MultipartBody.Part>
    ): ApiResponse

    @POST("/saveProjects")
    @Multipart
    suspend fun saveProjects(
        @Body projects: List<Project>,
        @Part images: List<MultipartBody.Part>
    ): ApiResponse

    @POST("/saveAsset")
    @Multipart
    suspend fun saveAsset(
        @Part asset: MultipartBody.Part,
        @Part images: List<MultipartBody.Part>
    ): ApiResponse
}
