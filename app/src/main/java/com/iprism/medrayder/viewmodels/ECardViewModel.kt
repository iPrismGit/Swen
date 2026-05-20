package com.iprism.medrayder.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.medrayder.models.address.AddAddressApiResponse
import com.iprism.medrayder.models.address.AddAddressRequest
import com.iprism.medrayder.models.contentpages.ContentPagesApiResponse
import com.iprism.medrayder.models.contentpages.ContentPagesRequest
import com.iprism.medrayder.models.dignosticcenters.DiagnosticCentersApiResponse
import com.iprism.medrayder.models.dignosticcenters.DiagnosticCentersRequest
import com.iprism.medrayder.models.ecard.ECardApiResponse
import com.iprism.medrayder.models.ecard.ECardRequest
import com.iprism.medrayder.models.homepage.HomePageApiResponse
import com.iprism.medrayder.models.homepage.HomePageRequest
import com.iprism.medrayder.models.labcenters.LabCentersApiResponse
import com.iprism.medrayder.models.labcenters.LabCentersRequest
import com.iprism.medrayder.repository.AddressRepository
import com.iprism.medrayder.repository.CommonRepository
import com.iprism.medrayder.repository.DiagnosticsRepository
import com.iprism.medrayder.repository.LabsRepository
import com.iprism.medrayder.repository.SubscriptionRepository
import com.iprism.medrayder.utils.UiState
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