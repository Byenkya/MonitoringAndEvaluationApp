package com.example.monitoringandevaluationapp.presentation.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.monitoringandevaluationapp.data.GroupEntity
import com.example.monitoringandevaluationapp.data.repository.GroupRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GroupViewModel(private val repository: GroupRepository): ViewModel() {
    // LiveData to hold the list of LocationEntity
    val allgroups: LiveData<List<GroupEntity>> = repository.getAllGroups()

    fun saveProjectGroup(groupEntity: GroupEntity) = viewModelScope.launch(
        Dispatchers.IO) {
        repository.saveGroup(groupEntity)
    }

    suspend fun getSavedGroupById(groupID: Long) : LiveData<GroupEntity>? {
        return repository.getGroupById(groupID)
    }


}