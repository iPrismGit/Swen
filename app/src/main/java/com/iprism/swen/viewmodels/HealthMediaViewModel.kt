package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.ecmuser.models.healthmedia.HealthMediaApiResponse
import com.iprism.ecmuser.models.healthmedia.HealthMediaRequest
import com.iprism.swen.repository.HealthMediaRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class HealthMediaViewModel(private val repository: HealthMediaRepository) : ViewModel()  {

    private val _response = MutableLiveData<UiState<HealthMediaApiResponse>>()
    val response: LiveData<UiState<HealthMediaApiResponse>> = _response

    fun fetchHealthMedia(request: HealthMediaRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.fetchHealthMedia(request)
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