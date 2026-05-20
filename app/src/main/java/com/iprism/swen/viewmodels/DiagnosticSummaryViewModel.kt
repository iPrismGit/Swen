package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.diagnosticbooking.DiagnosticBookingApiResponse
import com.iprism.swen.models.diagnosticbooking.DiagnosticBookingRequest
import com.iprism.swen.models.diagnostictimings.DiagnosticSlotsApiResponse
import com.iprism.swen.models.diagnostictimings.DiagnosticSlotsRequest
import com.iprism.swen.repository.DiagnosticsRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class DiagnosticSummaryViewModel(private val repository: DiagnosticsRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<DiagnosticSlotsApiResponse>>()
    val response: LiveData<UiState<DiagnosticSlotsApiResponse>> = _response

    private val _bookingResponse = MutableLiveData<UiState<DiagnosticBookingApiResponse>>()
    val bookingResponse: LiveData<UiState<DiagnosticBookingApiResponse>> = _bookingResponse

    fun getDetails(request: DiagnosticSlotsRequest) {
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

    fun bookDiagnostic(request: DiagnosticBookingRequest) {
        viewModelScope.launch {
            _bookingResponse.value = UiState.Loading
            try {
                val response = repository.bookDiagnostic(request)
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