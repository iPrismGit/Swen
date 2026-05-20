package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.hospitalmedbookingdetails.HospitalMedicineBookingDetailsApiResponse
import com.iprism.swen.models.hospitalmedbookingdetails.HospitalMedicineBookingDetailsRequest
import com.iprism.swen.models.pharmacyongoingbookings.PharmacyOnGoingBookingsApiResponse
import com.iprism.swen.models.pharmacyongoingbookings.PharmacyOnGoingBookingsRequest
import com.iprism.swen.repository.PharmaciesRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class PharmacyBookingsViewModel(private val repository: PharmaciesRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<PharmacyOnGoingBookingsApiResponse>>()
    val response : LiveData<UiState<PharmacyOnGoingBookingsApiResponse>> = _response

    private val _completedResponse = MutableLiveData<UiState<PharmacyOnGoingBookingsApiResponse>>()
    val completedResponse : LiveData<UiState<PharmacyOnGoingBookingsApiResponse>> = _completedResponse

    private val _bookingDetails = MutableLiveData<UiState<HospitalMedicineBookingDetailsApiResponse>>()
    val bookingDetails : LiveData<UiState<HospitalMedicineBookingDetailsApiResponse>> = _bookingDetails


    fun fetchPharmacyOngoingBookings(request: PharmacyOnGoingBookingsRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.fetchPharmacyOngoingBookings(request)
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

    fun fetchPharmacyCompletedBookings(request: PharmacyOnGoingBookingsRequest) {
        viewModelScope.launch {
            _completedResponse.value = UiState.Loading
            try {
                val response = repository.fetchPharmacyCompletedBookings(request)
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

    fun fetchPharmacyBookingDetails(request: HospitalMedicineBookingDetailsRequest) {
        viewModelScope.launch {
            _bookingDetails.value = UiState.Loading
            try {
                val response = repository.fetchPharmacyBookingDetails(request)
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