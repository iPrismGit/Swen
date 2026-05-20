package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.ecard.ECardApiResponse
import com.iprism.swen.models.ecard.ECardRequest
import com.iprism.swen.repository.SubscriptionRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class ECardViewModel(private val repository: SubscriptionRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<ECardApiResponse>>()
    val response: LiveData<UiState<ECardApiResponse>> = _response

    fun fetchECard(request: ECardRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.fetchECard(request)
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