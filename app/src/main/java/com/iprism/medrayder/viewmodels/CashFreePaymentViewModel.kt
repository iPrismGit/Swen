package com.iprism.medrayder.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.medrayder.models.address.AddAddressApiResponse
import com.iprism.medrayder.models.address.AddAddressRequest
import com.iprism.medrayder.models.checkpackage.CheckPaymentApiResponse
import com.iprism.medrayder.models.checkpackage.CheckPaymentRequest
import com.iprism.medrayder.models.createcashfreeorder.CreateCashFreeOrderApiResponse
import com.iprism.medrayder.models.createcashfreeorder.CreateCashFreeOrderRequest
import com.iprism.medrayder.models.dignosticcenters.DiagnosticCentersApiResponse
import com.iprism.medrayder.models.dignosticcenters.DiagnosticCentersRequest
import com.iprism.medrayder.models.homepage.HomePageApiResponse
import com.iprism.medrayder.models.homepage.HomePageRequest
import com.iprism.medrayder.models.labcenters.LabCentersApiResponse
import com.iprism.medrayder.models.labcenters.LabCentersRequest
import com.iprism.medrayder.repository.AddressRepository
import com.iprism.medrayder.repository.CashFreePaymentRepository
import com.iprism.medrayder.repository.CommonRepository
import com.iprism.medrayder.repository.DiagnosticsRepository
import com.iprism.medrayder.repository.LabsRepository
import com.iprism.medrayder.utils.UiState
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