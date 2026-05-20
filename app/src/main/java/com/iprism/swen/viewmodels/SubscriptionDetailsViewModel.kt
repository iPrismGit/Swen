package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.subscriptiondetails.SubscriptionDetailsApiResponse
import com.iprism.swen.models.subscriptiondetails.SubscriptionDetailsRequest
import com.iprism.swen.repository.SubscriptionRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class SubscriptionDetailsViewModel(private val repository: SubscriptionRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<SubscriptionDetailsApiResponse>>()
    val response: LiveData<UiState<SubscriptionDetailsApiResponse>> = _response

    fun fetchSubscriptionDetails(request: SubscriptionDetailsRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.fetchSubscriptionDetails(request)
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