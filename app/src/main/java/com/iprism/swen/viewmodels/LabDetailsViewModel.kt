package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.labtests.LabTestsApiResponse
import com.iprism.swen.models.labtests.LabTestsRequest
import com.iprism.swen.repository.LabsRepository
import com.iprism.swen.utils.UiState
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