package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.wallet.WalletApiResponse
import com.iprism.swen.models.wallet.WalletRequest
import com.iprism.swen.repository.WalletRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class WalletViewModel(private val repository: WalletRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<WalletApiResponse>>()
    val response: LiveData<UiState<WalletApiResponse>> = _response

    private val _addAmountResponse = MutableLiveData<UiState<WalletApiResponse>>()
    val addAmountResponse: LiveData<UiState<WalletApiResponse>> = _addAmountResponse

    fun wallet(request: WalletRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.wallet(request)
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

    fun addWalletAmount(request: WalletRequest) {
        viewModelScope.launch {
            _addAmountResponse.value = UiState.Loading
            try {
                val response = repository.wallet(request)
                if (response.status) {
                    _addAmountResponse.value = UiState.Success(response)
                } else {
                    _addAmountResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _addAmountResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}