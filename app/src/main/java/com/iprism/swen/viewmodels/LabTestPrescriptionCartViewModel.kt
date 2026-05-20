package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.labtestprescriptionbooking.LabTestPrescriptionBookingApiResponse
import com.iprism.swen.models.labtestprescriptionbooking.LabTestPrescriptionBookingRequest
import com.iprism.swen.repository.LabsRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class LabTestPrescriptionCartViewModel(private val repository: LabsRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<LabTestPrescriptionBookingApiResponse>>()
    val response: LiveData<UiState<LabTestPrescriptionBookingApiResponse>> = _response

    private val _bookingResponse = MutableLiveData<UiState<LabTestPrescriptionBookingApiResponse>>()
    val bookingResponse: LiveData<UiState<LabTestPrescriptionBookingApiResponse>> = _bookingResponse

    fun viewLabTestPrescription(request: LabTestPrescriptionBookingRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.bookPrescriptionLabTest(request)
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

    fun bookLabTestPrescription(request: LabTestPrescriptionBookingRequest) {
        viewModelScope.launch {
            _bookingResponse.value = UiState.Loading
            try {
                val response = repository.bookPrescriptionLabTest(request)
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