package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.labtestbookingdetails.LabTestBookingDetailsApiResponse
import com.iprism.swen.models.labtestbookingdetails.LabTestBookingDetailsRequest
import com.iprism.swen.models.labtestbookings.LabTestBookingsApiResponse
import com.iprism.swen.models.labtestbookings.LabTestBookingsRequest
import com.iprism.swen.repository.LabsRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class LabTestsBookingsViewModel(private val repository: LabsRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<LabTestBookingsApiResponse>>()
    val response : LiveData<UiState<LabTestBookingsApiResponse>> = _response

    private val _completedResponse = MutableLiveData<UiState<LabTestBookingsApiResponse>>()
    val completedResponse : LiveData<UiState<LabTestBookingsApiResponse>> = _completedResponse

    private val _bookingDetails = MutableLiveData<UiState<LabTestBookingDetailsApiResponse>>()
    val bookingDetails : LiveData<UiState<LabTestBookingDetailsApiResponse>> = _bookingDetails


    fun fetchLabTestsOngoingBookings(request: LabTestBookingsRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.fetchLabTestsOngoingBookings(request)
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

    fun fetchLabTestsCompletedBookings(request: LabTestBookingsRequest) {
        viewModelScope.launch {
            _completedResponse.value = UiState.Loading
            try {
                val response = repository.fetchLabTestsCompletedBookings(request)
                if (response.status) {
                    _completedResponse.value = UiState.Success(response)
                } else {
                    _completedResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _completedResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun fetchLabTestBookingDetails(request: LabTestBookingDetailsRequest) {
        viewModelScope.launch {
            _bookingDetails.value = UiState.Loading
            try {
                val response = repository.fetchLabTestsBookingDetails(request)
                if (response.status) {
                    _bookingDetails.value = UiState.Success(response)
                } else {
                    _bookingDetails.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _bookingDetails.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}