package com.example.monitoringandevaluationapp.data.repository

import android.content.Context
import android.util.Log
import com.example.monitoringandevaluationapp.data.ApiService
import com.example.monitoringandevaluationapp.data.api.model.ApiResponse
import com.example.monitoringandevaluationapp.data.api.model.Enterprise
import com.example.monitoringandevaluationapp.data.api.model.Group
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

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

    suspend fun getGroups(): List<Group> {
        return try {
            withContext(Dispatchers.IO) {
                apiService.getGroups()
            }
        } catch (e: Exception) {
            emptyList() // Handle the error appropriately
        }
    }
}

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetwork ?: return false
    val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
    return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}
