package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.pharmacies.PharmaciesApiResponse
import com.iprism.swen.models.pharmacies.PharmaciesRequest
import com.iprism.swen.repository.PharmaciesRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class PharmaciesViewModel(private val repository: PharmaciesRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<PharmaciesApiResponse>>()
    val response: LiveData<UiState<PharmaciesApiResponse>> = _response

    fun fetchPharmacies(request: PharmaciesRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.fetchPharmacies(request)
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