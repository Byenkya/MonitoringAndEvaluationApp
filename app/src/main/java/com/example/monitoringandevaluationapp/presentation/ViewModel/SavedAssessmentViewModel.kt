package com.example.monitoringandevaluationapp.presentation.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.monitoringandevaluationapp.data.SavedAssessmentEntity
import com.example.monitoringandevaluationapp.data.repository.savedAssessmentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SavedAssessmentViewModel(private val repository: savedAssessmentRepository) : ViewModel() {
    fun saveProjectAssessment(savedAssessmentEntity: SavedAssessmentEntity) = viewModelScope.launch(
        Dispatchers.IO) {
        repository.savedAssessmentID(savedAssessmentEntity)
    }

    suspend fun getSavedProjectAssessmentById(projectId: String) : SavedAssessmentEntity? {
        return repository.getSavedAssessmentById(projectId)
    }


}