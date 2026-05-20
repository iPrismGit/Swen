package com.iprism.medrayder.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.medrayder.models.contactus.ContactUsApiResponse
import com.iprism.medrayder.models.contactus.ContactUsRequest
import com.iprism.medrayder.repository.CommonRepository
import com.iprism.medrayder.utils.UiState
import kotlinx.coroutines.launch

class ContactUsViewModel(private val repository: CommonRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<ContactUsApiResponse>>()
    val response: LiveData<UiState<ContactUsApiResponse>> = _response

    private val _insertResponse = MutableLiveData<UiState<ContactUsApiResponse>>()
    val insertResponse: LiveData<UiState<ContactUsApiResponse>> = _insertResponse

    fun viewContactUs(request: ContactUsRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.contactUs(request)
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

    fun contactUs(request: ContactUsRequest) {
        viewModelScope.launch {
            _insertResponse.value = UiState.Loading
            try {
                val response = repository.contactUs(request)
                if (response.status) {
                    _insertResponse.value = UiState.Success(response)
                } else {
                    _insertResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _insertResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}