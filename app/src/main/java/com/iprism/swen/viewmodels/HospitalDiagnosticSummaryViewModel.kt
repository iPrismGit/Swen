package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.diagnostictimings.DiagnosticSlotsApiResponse
import com.iprism.swen.models.hospitaldiagnosticbooking.HospitalDiagnosticBookingApiResponse
import com.iprism.swen.models.hospitaldiagnosticbooking.HospitalDiagnosticBookingRequest
import com.iprism.swen.models.hospitalmedicinetimeslots.HospitalDiagnosticTimeRequest
import com.iprism.swen.repository.HospitalDiagnosticsRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class HospitalDiagnosticSummaryViewModel(private val repository: HospitalDiagnosticsRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<DiagnosticSlotsApiResponse>>()
    val response: LiveData<UiState<DiagnosticSlotsApiResponse>> = _response

    private val _bookingResponse = MutableLiveData<UiState<HospitalDiagnosticBookingApiResponse>>()
    val bookingResponse: LiveData<UiState<HospitalDiagnosticBookingApiResponse>> = _bookingResponse

    fun getDetails(request: HospitalDiagnosticTimeRequest) {
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

    fun bookDiagnostic(request: HospitalDiagnosticBookingRequest) {
        viewModelScope.launch {
            _bookingResponse.value = UiState.Loading
            try {
                val response = repository.bookHospitalDiagnostic(request)
                if (response.status) {
                    _bookingResponse.value = UiState.Success(response)
                } else {
                    _bookingResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _bookingResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}