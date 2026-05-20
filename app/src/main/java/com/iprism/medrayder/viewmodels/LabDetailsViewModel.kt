package com.iprism.medrayder.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.medrayder.models.diagnostictests.DiagnosticTestsApiResponse
import com.iprism.medrayder.models.diagnostictests.DiagnosticTestsRequest
import com.iprism.medrayder.models.dignosticcenters.DiagnosticCentersApiResponse
import com.iprism.medrayder.models.labtests.LabTestsApiResponse
import com.iprism.medrayder.models.labtests.LabTestsRequest
import com.iprism.medrayder.models.onlinedoctorbooking.OnlineDoctorBookingApiResponse
import com.iprism.medrayder.models.onlinedoctorbooking.OnlineDoctorBookingRequest
import com.iprism.medrayder.repository.DiagnosticsRepository
import com.iprism.medrayder.repository.LabsRepository
import com.iprism.medrayder.repository.OnlineDoctorRepository
import com.iprism.medrayder.utils.UiState
import kotlinx.coroutines.launch

class LabDetailsViewModel(private val repository: LabsRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<LabTestsApiResponse>>()
    val response: LiveData<UiState<LabTestsApiResponse>> = _response

    fun fetchLabTests(request: LabTestsRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.fetchLabs(request)
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