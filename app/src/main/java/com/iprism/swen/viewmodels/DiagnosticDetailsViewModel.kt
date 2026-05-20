package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.diagnostictests.DiagnosticTestsApiResponse
import com.iprism.swen.models.diagnostictests.DiagnosticTestsRequest
import com.iprism.swen.repository.DiagnosticsRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class DiagnosticDetailsViewModel(private val repository: DiagnosticsRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<DiagnosticTestsApiResponse>>()
    val response: LiveData<UiState<DiagnosticTestsApiResponse>> = _response

    fun fetchDiagnosticTests(request: DiagnosticTestsRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.fetchDiagnosticTests(request)
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