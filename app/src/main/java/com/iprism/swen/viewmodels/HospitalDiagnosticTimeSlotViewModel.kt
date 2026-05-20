package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.diagnostictimings.DiagnosticSlotsApiResponse
import com.iprism.swen.models.hospitalmedicinetimeslots.HospitalDiagnosticTimeRequest
import com.iprism.swen.repository.HospitalDiagnosticsRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class HospitalDiagnosticTimeSlotViewModel(private val repository: HospitalDiagnosticsRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<DiagnosticSlotsApiResponse>>()
    val response: LiveData<UiState<DiagnosticSlotsApiResponse>> = _response

    private val _response1 = MutableLiveData<UiState<DiagnosticSlotsApiResponse>>()
    val response1: LiveData<UiState<DiagnosticSlotsApiResponse>> = _response1

    fun getDates(request: HospitalDiagnosticTimeRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.fetchHospitalDiagnosticTimeSlots(request)
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

    fun getSlots(request: HospitalDiagnosticTimeRequest) {
        viewModelScope.launch {
            _response1.value = UiState.Loading
            try {
                val response = repository.fetchHospitalDiagnosticTimeSlots(request)
                if (response.status) {
                    _response1.value = UiState.Success(response)
                } else {
                    _response1.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _response1.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}