package com.example.monitoringandevaluationapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.monitoringandevaluationapp.data.LocationDao
import com.example.monitoringandevaluationapp.data.LocationEntity
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import java.io.File

class LocationRepository(private val locationDao: LocationDao) {
    suspend fun saveLocation(locationEntity: LocationEntity) {
        locationDao.insertLocation(locationEntity)
    }

    fun getAllLocations(): LiveData<List<LocationEntity>> {
        return locationDao.getAllLocations()
    }

    fun getLocationById(locationId: String): LiveData<LocationEntity> {
        return locationDao.getLocationById(locationId)
    }

    suspend fun deleteLocation(location: LocationEntity) {
        // delete images from the system
        deleteImages(location.memberPhotoPath)
        deleteImages(location.photoOnePath)
        deleteImages(location.photoTwoPath)
        deleteImages(location.photoThreePath)
        deleteImages(location.photoFourPath)
        deleteImages(location.milestonePhotoOnePath)
        deleteImages(location.milestonePhotoTwoPath)
        deleteImages(location.milestonePhotoThreePath)
        deleteImages(location.milestonePhotoFourPath)

        // finally delete the location
        locationDao.deleteLocationById(location.id)
    }

    private fun deleteImages(vararg filePaths: String?) {
        filePaths.filterNotNull().forEach { filePath ->
            val file = File(filePath)
            if (file.exists()) {
                file.delete()
            }

        }
    }
}
