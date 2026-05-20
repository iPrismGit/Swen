package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.addresslist.AddressListApiResponse
import com.iprism.swen.models.addresslist.AddressListRequest
import com.iprism.swen.repository.AddressRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class AddressListViewModel(private val repository: AddressRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<AddressListApiResponse>>()
    val response: LiveData<UiState<AddressListApiResponse>> = _response

    fun fetchAddressList(request: AddressListRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.fetchAddressList(request)
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