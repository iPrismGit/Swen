package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.hospitalmedbookingdetails.HospitalMedicineBookingDetailsApiResponse
import com.iprism.swen.models.hospitalmedbookingdetails.HospitalMedicineBookingDetailsRequest
import com.iprism.swen.models.hospitalmedicneongoing.HospitalMedicineOngoingBookingApiResponse
import com.iprism.swen.models.hospitalmedicneongoing.HospitalMedicineOngoingBookingRequest
import com.iprism.swen.repository.HospitalsMedicineRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class HospitalMedBookingsViewModel(private val repository: HospitalsMedicineRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<HospitalMedicineOngoingBookingApiResponse>>()
    val response : LiveData<UiState<HospitalMedicineOngoingBookingApiResponse>> = _response

    private val _completedResponse = MutableLiveData<UiState<HospitalMedicineOngoingBookingApiResponse>>()
    val completedResponse : LiveData<UiState<HospitalMedicineOngoingBookingApiResponse>> = _completedResponse

    private val _bookingDetails = MutableLiveData<UiState<HospitalMedicineBookingDetailsApiResponse>>()
    val bookingDetails : LiveData<UiState<HospitalMedicineBookingDetailsApiResponse>> = _bookingDetails


    fun fetchHospitalMedOngoingBookings(request: HospitalMedicineOngoingBookingRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.fetchHospitalMedOngoingBookings(request)
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

    fun fetchHospitalMedCompletedBookings(request: HospitalMedicineOngoingBookingRequest) {
        viewModelScope.launch {
            _completedResponse.value = UiState.Loading
            try {
                val response = repository.fetchHospitalMedCompletedBookings(request)
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

    fun fetchHospitalMedBookingDetails(request: HospitalMedicineBookingDetailsRequest) {
        viewModelScope.launch {
            _bookingDetails.value = UiState.Loading
            try {
                val response = repository.fetchHospitalMedBookingDetails(request)
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