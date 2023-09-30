package com.example.monitoringandevaluationapp.repository

import com.example.monitoringandevaluationapp.data.LocationDao
import com.example.monitoringandevaluationapp.data.LocationEntity

class LocationRepository(private val locationDao: LocationDao) {
    suspend fun saveLocation(locationEntity: LocationEntity) {
        locationDao.insertLocation(locationEntity)
    }
}
