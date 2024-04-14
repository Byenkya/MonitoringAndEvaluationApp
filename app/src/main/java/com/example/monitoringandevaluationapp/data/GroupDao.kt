package com.example.monitoringandevaluationapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GroupDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(groupEntity: GroupEntity)

    @Query("SELECT * FROM groupEntity")
    fun getAllGroups(): LiveData<List<GroupEntity>>

    @Query("SELECT * FROM groupEntity WHERE id = :groupId")
    fun getGroupById(groupId: Long): LiveData<GroupEntity>

    @Query("DELETE FROM groupEntity WHERE id = :groupId")
    suspend fun deleteGroupById(groupId: Long)
}