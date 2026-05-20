package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.labcenters.LabCentersApiResponse
import com.iprism.swen.models.labcenters.LabCentersRequest
import com.iprism.swen.repository.LabsRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class LabsViewModel(private val repository: LabsRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<LabCentersApiResponse>>()
    val response: LiveData<UiState<LabCentersApiResponse>> = _response

    fun fetchLabs(request: LabCentersRequest) {
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