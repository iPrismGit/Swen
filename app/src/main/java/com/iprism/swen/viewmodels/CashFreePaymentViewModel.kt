package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.checkpackage.CheckPaymentApiResponse
import com.iprism.swen.models.checkpackage.CheckPaymentRequest
import com.iprism.swen.models.createcashfreeorder.CreateCashFreeOrderApiResponse
import com.iprism.swen.models.createcashfreeorder.CreateCashFreeOrderRequest
import com.iprism.swen.repository.CashFreePaymentRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class CashFreePaymentViewModel(private val repository: CashFreePaymentRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<CreateCashFreeOrderApiResponse>>()
    val response: LiveData<UiState<CreateCashFreeOrderApiResponse>> = _response

    private val _checkPaymentResponse = MutableLiveData<UiState<CheckPaymentApiResponse>>()
    val checkPaymentResponse: LiveData<UiState<CheckPaymentApiResponse>> = _checkPaymentResponse

    fun createCashFreeOrder(request: CreateCashFreeOrderRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.createOrder(request)
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

    fun checkPayment(request: CheckPaymentRequest) {
        viewModelScope.launch {
            _checkPaymentResponse.value = UiState.Loading
            try {
                val response = repository.checkPayment(request)
                if (response.status) {
                    _checkPaymentResponse.value = UiState.Success(response)
                } else {
                    _checkPaymentResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _checkPaymentResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}