package com.iprism.medrayder.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.medrayder.models.diagnosticprescriptionbooking.DiagnosticPrescriptionBookingRequest
import com.iprism.medrayder.models.hospitaldiagnosticprescriptionbooking.HospitalDiagnosticPrescriptionBookingApiResponse
import com.iprism.medrayder.models.hospitaldiagnosticprescriptionbooking.HospitalDiagnosticPrescriptionBookingRequest
import com.iprism.medrayder.models.labtestprescriptionbooking.LabTestPrescriptionBookingApiResponse
import com.iprism.medrayder.models.labtestprescriptionbooking.LabTestPrescriptionBookingRequest
import com.iprism.medrayder.repository.DiagnosticsRepository
import com.iprism.medrayder.repository.HospitalDiagnosticsRepository
import com.iprism.medrayder.repository.LabsRepository
import com.iprism.medrayder.utils.UiState
import kotlinx.coroutines.launch

class HospitalDiagnosticTestPrescriptionCartViewModel(private val repository: HospitalDiagnosticsRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<HospitalDiagnosticPrescriptionBookingApiResponse>>()
    val response: LiveData<UiState<HospitalDiagnosticPrescriptionBookingApiResponse>> = _response

    private val _bookingResponse = MutableLiveData<UiState<HospitalDiagnosticPrescriptionBookingApiResponse>>()
    val bookingResponse: LiveData<UiState<HospitalDiagnosticPrescriptionBookingApiResponse>> = _bookingResponse

    fun viewHospitalDiagnosticTestPrescription(request: HospitalDiagnosticPrescriptionBookingRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.bookHospitalDiagnosticPrescriptionBooking(request)
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

    fun bookHospitalDiagnosticTestPrescription(request: HospitalDiagnosticPrescriptionBookingRequest) {
        viewModelScope.launch {
            _bookingResponse.value = UiState.Loading
            try {
                val response = repository.bookHospitalDiagnosticPrescriptionBooking(request)
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