package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.pharmacyprescriptionbooking.PharmacyPrescriptionBookingApiResponse
import com.iprism.swen.models.pharmacyprescriptionbooking.PharmacyPrescriptionBookingRequest
import com.iprism.swen.repository.PharmaciesRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class PharmacyPrescriptionCartViewModel(private val repository: PharmaciesRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<PharmacyPrescriptionBookingApiResponse>>()
    val response: LiveData<UiState<PharmacyPrescriptionBookingApiResponse>> = _response

    private val _bookingResponse = MutableLiveData<UiState<PharmacyPrescriptionBookingApiResponse>>()
    val bookingResponse: LiveData<UiState<PharmacyPrescriptionBookingApiResponse>> = _bookingResponse

    fun viewPharmacyPrescriptionOrder(request: PharmacyPrescriptionBookingRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.bookPharmacyPrescriptionOrder(request)
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

    fun bookPharmacyPrescriptionOrder(request: PharmacyPrescriptionBookingRequest) {
        viewModelScope.launch {
            _bookingResponse.value = UiState.Loading
            try {
                val response = repository.bookPharmacyPrescriptionOrder(request)
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