package com.example.monitoringandevaluationapp.presentation.ViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.monitoringandevaluationapp.data.GroupEntity
import com.example.monitoringandevaluationapp.data.api.model.Group
import com.example.monitoringandevaluationapp.data.repository.GroupRepository
import com.example.monitoringandevaluationapp.data.repository.PostProjectGroupRepository
import com.example.monitoringandevaluationapp.data.repository.isNetworkAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GroupViewModel(
    context: Context,
    private val repository: GroupRepository,
    private val fromServerRepo: PostProjectGroupRepository
): ViewModel() {

    // LiveData to hold the list of LocationEntity
    val allgroups: LiveData<List<GroupEntity>> = repository.getAllGroups()

    val groupsFromServer = MutableLiveData<List<Group>>()

    init {
        viewModelScope.launch {
            fetchAndSaveGroups(context)
        }
    }

    fun fetchGroups() {
        viewModelScope.launch {
            val groupList = fromServerRepo.getGroups()
            groupsFromServer.postValue(groupList)
        }
    }

    suspend fun fetchAndSaveGroups(context: Context) {
        Log.e("Error: >>>>>>>>>", "Excuting code${isNetworkAvailable(context)}")
        if (isNetworkAvailable(context)) {
            try {
                repository.deleteAllGroups()
                val groupsFromServer = fromServerRepo.getGroups()
                groupsFromServer.forEach { group ->
                    val groupEntity = GroupEntity(
                        id = group.id,
                        name = group.name,
                        descr = group.descr
                    )
                    repository.saveGroup(groupEntity)
                }
            } catch (e: Exception) {
                Log.e("Error: >>>>>>>>>", "${e.message}")
                // Handle exception
            }
        }

    }

    fun saveProjectGroup(groupEntity: GroupEntity) = viewModelScope.launch(
        Dispatchers.IO) {
        repository.saveGroup(groupEntity)
    }

    suspend fun getSavedGroupById(groupID: Long) : LiveData<GroupEntity>? {
        return repository.getGroupById(groupID)
    }
}