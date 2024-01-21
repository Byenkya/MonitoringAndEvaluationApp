package com.example.monitoringandevaluationapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: LocationEntity)

    @Query("SELECT * FROM locationEntity")
    fun getAllLocations(): LiveData<List<LocationEntity>>

    @Query("SELECT * FROM locationentity WHERE id = :locationId")
    fun getLocationById(locationId: String): LiveData<LocationEntity>

    @Query("DELETE FROM locationEntity WHERE id = :locationId")
    suspend fun deleteLocationById(locationId: String)
}
