package com.example.monitoringandevaluationapp.data.repository

import androidx.lifecycle.LiveData
import com.example.monitoringandevaluationapp.data.GroupDao
import com.example.monitoringandevaluationapp.data.GroupEntity
import com.example.monitoringandevaluationapp.data.LocationEntity

class GroupRepository(private val groupDao: GroupDao) {
    suspend fun saveGroup(groupEntity: GroupEntity) {
        groupDao.insertGroup(groupEntity)
    }

    fun getAllGroups(): LiveData<List<GroupEntity>> {
        return groupDao.getAllGroups()
    }

    fun getGroupById(groupId: Long): LiveData<GroupEntity> {
        return groupDao.getGroupById(groupId)
    }

    suspend fun deleteGroup(groupEntity: GroupEntity) {
        // finally delete the location
        groupDao.deleteGroupById(groupEntity.id)
    }

}