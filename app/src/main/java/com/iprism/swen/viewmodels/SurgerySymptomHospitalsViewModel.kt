package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.hospitals.HospitalsApiResponse
import com.iprism.swen.models.surgerysymptoms.SurgerySymptomsDoctorRequest
import com.iprism.swen.repository.SurgerySymptomsRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class SurgerySymptomHospitalsViewModel(private val repository: SurgerySymptomsRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<HospitalsApiResponse>>()
    val response: LiveData<UiState<HospitalsApiResponse>> = _response

    fun getHospitals(request: SurgerySymptomsDoctorRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.fetchSurgerySymptomHospitals(request)
                if (response.status) {
                    _response.value = UiState.Success(response)
                } else {
                    _response.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _response.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}