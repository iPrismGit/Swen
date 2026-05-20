package com.iprism.medrayder.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.medrayder.models.hospitaldoctors.HospitalDoctorsApiResponse
import com.iprism.medrayder.models.hospitaldoctors.HospitalDoctorsRequest
import com.iprism.medrayder.repository.HospitalsRepository
import com.iprism.medrayder.utils.UiState
import kotlinx.coroutines.launch

class HospitalDoctorsViewModel(private val repository: HospitalsRepository) : ViewModel() {

    private val _doctors = MutableLiveData<UiState<HospitalDoctorsApiResponse>>()
    val doctors : LiveData<UiState<HospitalDoctorsApiResponse>> = _doctors


    fun getDoctors(request: HospitalDoctorsRequest) {
        viewModelScope.launch {
            _doctors.value = UiState.Loading
            try {
                val response = repository.getHospitalDoctors(request)
                if (response.status) {
                    _doctors.value = UiState.Success(response)
                } else {
                    _doctors.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _doctors.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}