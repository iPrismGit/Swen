package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.diagnosticbookingdetails.DiagnosticTestBookingDetailsApiResponse
import com.iprism.swen.models.diagnosticbookingdetails.DiagnosticTestBookingDetailsRequest
import com.iprism.swen.models.diagnostictestbookings.DiagnosticTestsBookingsApiResponse
import com.iprism.swen.models.labtestbookings.LabTestBookingsRequest
import com.iprism.swen.repository.DiagnosticsRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class DiagnosticTestsBookingsViewModel(private val repository: DiagnosticsRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<DiagnosticTestsBookingsApiResponse>>()
    val response : LiveData<UiState<DiagnosticTestsBookingsApiResponse>> = _response

    private val _completedResponse = MutableLiveData<UiState<DiagnosticTestsBookingsApiResponse>>()
    val completedResponse : LiveData<UiState<DiagnosticTestsBookingsApiResponse>> = _completedResponse

    private val _bookingDetails = MutableLiveData<UiState<DiagnosticTestBookingDetailsApiResponse>>()
    val bookingDetails : LiveData<UiState<DiagnosticTestBookingDetailsApiResponse>> = _bookingDetails


    fun fetchDiagnosticTestsOngoingBookings(request: LabTestBookingsRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.fetchDiagnosticTestsOngoingBookings(request)
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

    fun fetchDiagnosticTestsCompletedBookings(request: LabTestBookingsRequest) {
        viewModelScope.launch {
            _completedResponse.value = UiState.Loading
            try {
                val response = repository.fetchDiagnosticTestsCompletedBookings(request)
                if (response.status) {
                    _completedResponse.value = UiState.Success(response)
                } else {
                    _completedResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _completedResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun fetchDiagnosticTestBookingDetails(request: DiagnosticTestBookingDetailsRequest) {
        viewModelScope.launch {
            _bookingDetails.value = UiState.Loading
            try {
                val response = repository.fetchDiagnosticTestBookingDetails(request)
                if (response.status) {
                    _bookingDetails.value = UiState.Success(response)
                } else {
                    _bookingDetails.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _bookingDetails.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}