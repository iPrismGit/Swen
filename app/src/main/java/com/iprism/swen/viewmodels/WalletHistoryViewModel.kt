package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.wallethistory.WalletHistoryApiResponse
import com.iprism.swen.models.wallethistory.WalletHistoryRequest
import com.iprism.swen.repository.WalletRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class WalletHistoryViewModel(private val repository: WalletRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<WalletHistoryApiResponse>>()
    val response: LiveData<UiState<WalletHistoryApiResponse>> = _response

    fun fetchWalletHistory(request: WalletHistoryRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.fetchWalletHistory(request)
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