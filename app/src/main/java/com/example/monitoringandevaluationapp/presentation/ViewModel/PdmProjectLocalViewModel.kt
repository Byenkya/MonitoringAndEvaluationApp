package com.example.monitoringandevaluationapp.presentation.ViewModel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.monitoringandevaluationapp.data.LocationEntity
import com.example.monitoringandevaluationapp.data.PdmProjectAssessmentEntity
import com.example.monitoringandevaluationapp.data.PdmProjectEntity
import com.example.monitoringandevaluationapp.data.repository.PdmProjectAssessmentRepository
import com.example.monitoringandevaluationapp.data.repository.PdmProjectLocalRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PdmProjectLocalViewModel(
    private val repository: PdmProjectLocalRepository,
    private val pdmProjectAssessmentRepository: PdmProjectAssessmentRepository
): ViewModel() {
    // LiveData to hold the list of LocationEntity
    val allPdmLocalProjects: LiveData<List<PdmProjectEntity>> = repository.getAllPdmProjects()

    val allProjectAssessments: LiveData<List<PdmProjectAssessmentEntity>> = pdmProjectAssessmentRepository.getAllAssessments()

    fun savePdmProject(pdmProject: PdmProjectEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.savePdmProject(pdmProject)
    }

    fun getPdmProjectById(projectId: Long): LiveData<PdmProjectEntity> {
        return repository.getPdmProjectById(projectId)
    }

    fun markProjectAsUploaded(projectId: Long) = viewModelScope.launch(Dispatchers.IO){
        repository.markProjectUploaded(projectId)
    }

    fun deletePdmProject(pdmProject: PdmProjectEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.deletePdmProject(pdmProject)
    }


    private val _userLocation = MutableLiveData<LatLng>()
    val userLocation: LiveData<LatLng> get() = _userLocation

    fun updateUserLocation(newLocation: LatLng) {
        _userLocation.value = newLocation
    }

    // Assessed photo 1
    private val _assessedPhotoOne = MutableStateFlow<Uri?>(null)
    val assessedPhotoOneUri = _assessedPhotoOne.asStateFlow()
    fun updateAssessedPhotoOneUri(uri: Uri?) {
        viewModelScope.launch {
            _assessedPhotoOne.value = uri
        }
    }

    // Assessed photo 2
    private val _assessedPhotoTwo = MutableStateFlow<Uri?>(null)
    val assessedPhotoTwoUri = _assessedPhotoTwo.asStateFlow()
    fun updateAssessedPhotoTwoUri(uri: Uri?) {
        viewModelScope.launch {
            _assessedPhotoTwo.value = uri
        }
    }

    // Assessed photo 3
    private val _assessedPhotoThree = MutableStateFlow<Uri?>(null)
    val assessedPhotoThreeUri = _assessedPhotoThree.asStateFlow()
    fun updateAssessedPhotoThreeUri(uri: Uri?) {
        viewModelScope.launch {
            _assessedPhotoThree.value = uri
        }
    }

    // Assessed photo 4
    private val _assessedPhotoFour = MutableStateFlow<Uri?>(null)
    val assessedPhotoFourUri = _assessedPhotoFour.asStateFlow()
    fun updateAssessedPhotoFourUri(uri: Uri?) {
        viewModelScope.launch {
            _assessedPhotoFour.value = uri
        }
    }

    // milestone photo 1
    private val _milestonePhotoOne = MutableStateFlow<Uri?>(null)
    val milestonePhotoOneUri = _milestonePhotoOne.asStateFlow()
    fun updateMilestonePhotoOneUri(uri: Uri?) {
        viewModelScope.launch {
            _milestonePhotoOne.value = uri
        }
    }

    // milestone photo 2
    private val _milestonePhotoTwo = MutableStateFlow<Uri?>(null)
    val milestonePhotoTwoUri = _milestonePhotoTwo.asStateFlow()
    fun updateMilestonePhotoTwoUri(uri: Uri?) {
        viewModelScope.launch {
            _milestonePhotoTwo.value = uri
        }
    }

    // milestone photo 3
    private val _milestonePhotoThree = MutableStateFlow<Uri?>(null)
    val milestonePhotoThreeUri = _milestonePhotoThree.asStateFlow()
    fun updateMilestonePhotoThreeUri(uri: Uri?) {
        viewModelScope.launch {
            _milestonePhotoThree.value = uri
        }
    }

    // milestone photo 4
    private val _milestonePhotoFour = MutableStateFlow<Uri?>(null)
    val milestonePhotoFourUri = _milestonePhotoFour.asStateFlow()
    fun updateMilestonePhotoFourUri(uri: Uri?) {
        viewModelScope.launch {
            _milestonePhotoFour.value = uri
        }
    }

}