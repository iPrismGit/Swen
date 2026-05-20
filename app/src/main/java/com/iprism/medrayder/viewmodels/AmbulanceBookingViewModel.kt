package com.iprism.medrayder.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.medrayder.models.ambulancebooking.AmbulanceBookingApiResponse
import com.iprism.medrayder.models.ambulancebooking.AmbulanceBookingRequest
import com.iprism.medrayder.models.diagnosticbooking.DiagnosticBookingApiResponse
import com.iprism.medrayder.models.diagnosticbooking.DiagnosticBookingRequest
import com.iprism.medrayder.models.diagnostictimings.DiagnosticSlotsApiResponse
import com.iprism.medrayder.models.diagnostictimings.DiagnosticSlotsRequest
import com.iprism.medrayder.models.familymembers.FamilyMembersApiResponse
import com.iprism.medrayder.models.familymembers.FamilyMembersRequest
import com.iprism.medrayder.repository.CommonRepository
import com.iprism.medrayder.repository.DiagnosticsRepository
import com.iprism.medrayder.repository.HospitalsRepository
import com.iprism.medrayder.utils.UiState
import kotlinx.coroutines.launch

class AmbulanceBookingViewModel(private val repository: HospitalsRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<AmbulanceBookingApiResponse>>()
    val response: LiveData<UiState<AmbulanceBookingApiResponse>> = _response

    private val _bookingResponse = MutableLiveData<UiState<AmbulanceBookingApiResponse>>()
    val bookingResponse: LiveData<UiState<AmbulanceBookingApiResponse>> = _bookingResponse

    fun viewHospitalAmbulanceBooking(request: AmbulanceBookingRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.bookHospitalAmbulance(request)
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

    fun bookHospitalAmbulance(request: AmbulanceBookingRequest) {
        viewModelScope.launch {
            _bookingResponse.value = UiState.Loading
            try {
                val response = repository.bookHospitalAmbulance(request)
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