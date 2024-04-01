package com.example.monitoringandevaluationapp.presentation.ViewModel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PDMViewModel: ViewModel(){
    // Asset photo 1
    private val _assetPhotoOneUri = MutableStateFlow<Uri?>(null)
    val assetPhotoOneUri = _assetPhotoOneUri.asStateFlow()
    fun updateAssetPhotoOneUri(uri: Uri?) {
        viewModelScope.launch {
            _assetPhotoOneUri.value = uri
        }

    }

    // Asset photo 2
    private val _assetPhotoTwoUri = MutableStateFlow<Uri?>(null)
    val assetPhotoTwoUri = _assetPhotoTwoUri.asStateFlow()
    fun updateAssetPhotoTwoUri(uri: Uri?) {
        viewModelScope.launch {
            _assetPhotoTwoUri.value = uri
        }

    }

    // Date Acquired
    private val _dateAcquired = MutableStateFlow("")
    val dateAcquired get() = _dateAcquired
    fun updateDateAcquired(date: String) {
        viewModelScope.launch {
            _dateAcquired.value = date
        }
    }

}