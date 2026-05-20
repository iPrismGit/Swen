package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.subscription.SubscriptionApiResponse
import com.iprism.swen.models.subscription.SubscriptionRequest
import com.iprism.swen.repository.SubscriptionRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class SubscriptionViewModel(private val repository: SubscriptionRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<SubscriptionApiResponse>>()
    val response: LiveData<UiState<SubscriptionApiResponse>> = _response

    private val _billSummaryResponse = MutableLiveData<UiState<SubscriptionApiResponse>>()
    val billSummaryResponse: LiveData<UiState<SubscriptionApiResponse>> = _billSummaryResponse

    private val _paymentResponse = MutableLiveData<UiState<SubscriptionApiResponse>>()
    val paymentResponse : LiveData<UiState<SubscriptionApiResponse>> = _paymentResponse

    fun subscription(request: SubscriptionRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.subscription(request)
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

    fun fetchBillSummary(request: SubscriptionRequest) {
        viewModelScope.launch {
            _billSummaryResponse.value = UiState.Loading
            try {
                val response = repository.subscription(request)
                if (response.status) {
                    _billSummaryResponse.value = UiState.Success(response)
                } else {
                    _billSummaryResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _billSummaryResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun subscribe(request: SubscriptionRequest) {
        viewModelScope.launch {
            _paymentResponse.value = UiState.Loading
            try {
                val response = repository.subscription(request)
                if (response.status) {
                    _paymentResponse.value = UiState.Success(response)
                } else {
                    _paymentResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _paymentResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}