package com.iprism.medrayder.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.medrayder.models.diagnostictimings.DiagnosticSlotsApiResponse
import com.iprism.medrayder.models.diagnostictimings.DiagnosticSlotsRequest
import com.iprism.medrayder.models.login.LoginApiResponse
import com.iprism.medrayder.models.login.LoginRequest
import com.iprism.medrayder.models.onlinedoctorbookingdetails.OnlineDoctorBookingDetailsApiResponse
import com.iprism.medrayder.models.onlinedoctorbookingdetails.OnlineDoctorBookingDetailsRequest
import com.iprism.medrayder.models.resendotp.ResendOtpApiResponse
import com.iprism.medrayder.repository.DiagnosticsRepository
import com.iprism.medrayder.repository.OnlineDoctorRepository
import com.iprism.medrayder.utils.UiState
import kotlinx.coroutines.launch

class DiagnosticTimeSlotViewModel(private val repository: DiagnosticsRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<DiagnosticSlotsApiResponse>>()
    val response: LiveData<UiState<DiagnosticSlotsApiResponse>> = _response

    private val _response1 = MutableLiveData<UiState<DiagnosticSlotsApiResponse>>()
    val response1: LiveData<UiState<DiagnosticSlotsApiResponse>> = _response1

    fun getDates(request: DiagnosticSlotsRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.fetchDiagnosticBookingDetails(request)
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

    fun getSlots(request: DiagnosticSlotsRequest) {
        viewModelScope.launch {
            _response1.value = UiState.Loading
            try {
                val response = repository.fetchDiagnosticBookingDetails(request)
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