package com.iprism.medrayder.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.medrayder.models.ambulancebooking.AmbulanceBookingApiResponse
import com.iprism.medrayder.models.ambulancebooking.AmbulanceBookingRequest
import com.iprism.medrayder.models.ambulancetracking.AmbulanceTrackingApiResponse
import com.iprism.medrayder.models.ambulancetracking.AmbulanceTrackingRequest
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

class AmbulanceTrackingViewModel(private val repository: HospitalsRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<AmbulanceTrackingApiResponse>>()
    val response: LiveData<UiState<AmbulanceTrackingApiResponse>> = _response

    fun trackAmbulance(request: AmbulanceTrackingRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.trackAmbulance(request)
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