package com.iprism.medrayder.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.medrayder.models.admit.AdmitBookingApiResponse
import com.iprism.medrayder.models.admit.AdmitBookingRequest
import com.iprism.medrayder.repository.HospitalsRepository
import com.iprism.medrayder.utils.UiState
import kotlinx.coroutines.launch

class HospitalAdmitViewModel(private val repository: HospitalsRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<AdmitBookingApiResponse>>()
    val response: LiveData<UiState<AdmitBookingApiResponse>> = _response

    private val _slots = MutableLiveData<UiState<AdmitBookingApiResponse>>()
    val slots: LiveData<UiState<AdmitBookingApiResponse>> = _slots

    private val _bookingResponse = MutableLiveData<UiState<AdmitBookingApiResponse>>()
    val bookingResponse: LiveData<UiState<AdmitBookingApiResponse>> = _bookingResponse

    fun getDates(request: AdmitBookingRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.bookHospitalAdmit(request)
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

    fun getSlots(request: AdmitBookingRequest) {
        viewModelScope.launch {
            _slots.value = UiState.Loading
            try {
                val response = repository.bookHospitalAdmit(request)
                if (response.status) {
                    _slots.value = UiState.Success(response)
                } else {
                    _slots.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _slots.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun bookHospitalAdmit(request: AdmitBookingRequest) {
        viewModelScope.launch {
            _bookingResponse.value = UiState.Loading
            try {
                val response = repository.bookHospitalAdmit(request)
                if (response.status) {
                    _bookingResponse.value = UiState.Success(response)
                } else {
                    _bookingResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _bookingResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}