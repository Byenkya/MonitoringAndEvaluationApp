package com.example.monitoringandevaluationapp.presentation.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.monitoringandevaluationapp.data.api.model.ApiResponse
import com.example.monitoringandevaluationapp.domain.usecases.FetchAndPostProjectUseCaseImpl
import kotlinx.coroutines.launch

class ProjectViewModel(
    private val fetchAndPostProjectUseCaseImpl: FetchAndPostProjectUseCaseImpl
) : ViewModel() {
    private val _apiResponse = MutableLiveData<ApiResponse>()
    val apiResponse: LiveData<ApiResponse> get() = _apiResponse

    fun postProject() {
        viewModelScope.launch {
            try {
                val response = fetchAndPostProjectUseCaseImpl.execute();
                _apiResponse.value = response

            } catch (e: Exception) {
                // Handle exceptions
                _apiResponse.value = ApiResponse("Error: ${e.message}")
            }
        }
    }
}