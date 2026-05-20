package com.iprism.medrayder.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.medrayder.models.dignosticcenters.DiagnosticCentersApiResponse
import com.iprism.medrayder.models.dignosticcenters.DiagnosticCentersRequest
import com.iprism.medrayder.repository.DiagnosticsRepository
import com.iprism.medrayder.utils.UiState
import kotlinx.coroutines.launch

class DiagnosticsViewModel(private val repository: DiagnosticsRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<DiagnosticCentersApiResponse>>()
    val response: LiveData<UiState<DiagnosticCentersApiResponse>> = _response

    fun fetchDiagnosticCenters(request: DiagnosticCentersRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.getDiagnostics(request)
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