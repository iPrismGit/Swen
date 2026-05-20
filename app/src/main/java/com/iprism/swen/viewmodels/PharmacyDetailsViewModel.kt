package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.pharmacyDetails.PharmacyDetailsApiResponse
import com.iprism.swen.models.pharmacyDetails.PharmacyDetailsRequest
import com.iprism.swen.repository.PharmaciesRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class PharmacyDetailsViewModel(private val repository: PharmaciesRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<PharmacyDetailsApiResponse>>()
    val response: LiveData<UiState<PharmacyDetailsApiResponse>> = _response

    fun fetchPharmacyDetails(request: PharmacyDetailsRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.fetchPharmacyDetails(request)
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