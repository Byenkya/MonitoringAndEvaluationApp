package com.example.monitoringandevaluationapp.usecases

import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.monitoringandevaluationapp.data.LocationEntity
import com.example.monitoringandevaluationapp.repository.LocationRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.Manifest
import android.location.Location
import android.location.LocationListener

class LocationViewModel(private val repository: LocationRepository) : ViewModel() {

    // LiveData to hold the list of LocationEntity
    val allLocations: LiveData<List<LocationEntity>> = repository.getAllLocations()

    fun saveLocation(locationEntity: LocationEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.saveLocation(locationEntity)
    }

    private val _userLocation = MutableLiveData<LatLng>()
    val userLocation: LiveData<LatLng> get() = _userLocation

    fun updateUserLocation(newLocation: LatLng) {
        _userLocation.value = newLocation
    }

}
