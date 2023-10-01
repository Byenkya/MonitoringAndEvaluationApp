package com.example.monitoringandevaluationapp.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.monitoringandevaluationapp.data.LocationEntity
import com.example.monitoringandevaluationapp.repository.LocationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
class LocationViewModel(private val repository: LocationRepository) : ViewModel() {

    // LiveData to hold the list of LocationEntity
    val allLocations: LiveData<List<LocationEntity>> = repository.getAllLocations()

    fun saveLocation(locationEntity: LocationEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.saveLocation(locationEntity)
    }
}
