package com.example.monitoringandevaluationapp.presentation.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.monitoringandevaluationapp.data.api.model.ApiResponse
import com.example.monitoringandevaluationapp.domain.usecases.FetchAndPostLocationUseCase
import com.example.monitoringandevaluationapp.domain.usecases.FetchAndPostLocationUseCaseImpl
import kotlinx.coroutines.launch

class MainViewModel(
    private val fetchAndPostLocationUseCaseImpl: FetchAndPostLocationUseCaseImpl
) : ViewModel() {

    private val _apiResponse = MutableLiveData<ApiResponse>()
    val apiResponse: LiveData<ApiResponse> get() = _apiResponse

    fun fetchDataAndPost() {
        viewModelScope.launch {
            try {
                // Execute the use case to fetch and post data
                val response = fetchAndPostLocationUseCaseImpl.execute()
                _apiResponse.value = response
            } catch (e: Exception) {
                // Handle exceptions
                _apiResponse.value = ApiResponse("Error: ${e.message}")
            }
        }
    }
}