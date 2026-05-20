package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.address.AddAddressApiResponse
import com.iprism.swen.models.address.AddAddressRequest
import com.iprism.swen.repository.AddressRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class AddAddressViewModel(private val repository: AddressRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<AddAddressApiResponse>>()
    val response: LiveData<UiState<AddAddressApiResponse>> = _response

    fun addAddress(request: AddAddressRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.addAddress(request)
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