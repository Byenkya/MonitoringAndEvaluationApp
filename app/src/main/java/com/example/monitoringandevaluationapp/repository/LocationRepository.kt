package com.example.monitoringandevaluationapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.monitoringandevaluationapp.data.LocationDao
import com.example.monitoringandevaluationapp.data.LocationEntity
import kotlinx.coroutines.flow.Flow

class LocationRepository(private val locationDao: LocationDao) {
    suspend fun saveLocation(locationEntity: LocationEntity) {
        locationDao.insertLocation(locationEntity)
    }

    fun getAllLocations(): LiveData<List<LocationEntity>> {
        return locationDao.getAllLocations()
    }
}
