package com.iprism.medrayder.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.medrayder.models.dignosticcenters.DiagnosticCentersApiResponse
import com.iprism.medrayder.models.dignosticcenters.DiagnosticCentersRequest
import com.iprism.medrayder.models.homepage.HomePageApiResponse
import com.iprism.medrayder.models.homepage.HomePageRequest
import com.iprism.medrayder.models.labcenters.LabCentersApiResponse
import com.iprism.medrayder.models.labcenters.LabCentersRequest
import com.iprism.medrayder.models.wallet.WalletApiResponse
import com.iprism.medrayder.models.wallet.WalletRequest
import com.iprism.medrayder.models.wallethistory.WalletHistoryApiResponse
import com.iprism.medrayder.models.wallethistory.WalletHistoryRequest
import com.iprism.medrayder.repository.CommonRepository
import com.iprism.medrayder.repository.DiagnosticsRepository
import com.iprism.medrayder.repository.LabsRepository
import com.iprism.medrayder.repository.WalletRepository
import com.iprism.medrayder.utils.UiState
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