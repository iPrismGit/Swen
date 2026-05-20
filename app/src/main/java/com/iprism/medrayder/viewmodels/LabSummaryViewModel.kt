package com.iprism.medrayder.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.medrayder.models.diagnosticbooking.DiagnosticBookingApiResponse
import com.iprism.medrayder.models.diagnosticbooking.DiagnosticBookingRequest
import com.iprism.medrayder.models.diagnostictimings.DiagnosticSlotsApiResponse
import com.iprism.medrayder.models.diagnostictimings.DiagnosticSlotsRequest
import com.iprism.medrayder.models.labbooking.LabBookingApiResponse
import com.iprism.medrayder.models.labbooking.LabBookingRequest
import com.iprism.medrayder.models.labtestslots.LabTestSlotsApiResponse
import com.iprism.medrayder.models.labtestslots.LabTestSlotsRequest
import com.iprism.medrayder.repository.DiagnosticsRepository
import com.iprism.medrayder.repository.LabsRepository
import com.iprism.medrayder.utils.UiState
import kotlinx.coroutines.launch

class LabSummaryViewModel(private val repository: LabsRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<LabTestSlotsApiResponse>>()
    val response: LiveData<UiState<LabTestSlotsApiResponse>> = _response

    private val _bookingResponse = MutableLiveData<UiState<LabBookingApiResponse>>()
    val bookingResponse: LiveData<UiState<LabBookingApiResponse>> = _bookingResponse

    fun getDetails(request: LabTestSlotsRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.fetchLabTestBookingDetails(request)
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

    fun bookLab(request: LabBookingRequest) {
        viewModelScope.launch {
            _bookingResponse.value = UiState.Loading
            try {
                val response = repository.bookLab(request)
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