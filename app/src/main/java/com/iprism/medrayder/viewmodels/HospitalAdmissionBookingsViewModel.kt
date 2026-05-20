package com.iprism.medrayder.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.medrayder.models.hospitaladmitongoingbookings.HospitalAdmissionOnGoingApiResponse
import com.iprism.medrayder.models.hospitaladmitongoingbookings.HospitalAdmissionOnGoingRequest
import com.iprism.medrayder.models.hospitalmedbookingdetails.HospitalMedicineBookingDetailsApiResponse
import com.iprism.medrayder.models.hospitalmedbookingdetails.HospitalMedicineBookingDetailsRequest
import com.iprism.medrayder.models.hospitalmedicneongoing.HospitalMedicineOngoingBookingApiResponse
import com.iprism.medrayder.models.hospitalmedicneongoing.HospitalMedicineOngoingBookingRequest
import com.iprism.medrayder.repository.HospitalsMedicineRepository
import com.iprism.medrayder.repository.HospitalsRepository
import com.iprism.medrayder.utils.UiState
import kotlinx.coroutines.launch

class HospitalAdmissionBookingsViewModel(private val repository: HospitalsRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<HospitalAdmissionOnGoingApiResponse>>()
    val response : LiveData<UiState<HospitalAdmissionOnGoingApiResponse>> = _response

    private val _completedResponse = MutableLiveData<UiState<HospitalAdmissionOnGoingApiResponse>>()
    val completedResponse : LiveData<UiState<HospitalAdmissionOnGoingApiResponse>> = _completedResponse

    private val _bookingDetails = MutableLiveData<UiState<HospitalMedicineBookingDetailsApiResponse>>()
    val bookingDetails : LiveData<UiState<HospitalMedicineBookingDetailsApiResponse>> = _bookingDetails


    fun fetchHospitalAdmissionOngoingBookings(request: HospitalAdmissionOnGoingRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.fetchHospitalAdmissionOngoingBookings(request)
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

    fun fetchHospitalAdmissionCompletedBookings(request: HospitalAdmissionOnGoingRequest) {
        viewModelScope.launch {
            _completedResponse.value = UiState.Loading
            try {
                val response = repository.fetchHospitalAdmissionCompletedBookings(request)
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

   /* fun fetchHospitalMedBookingDetails(request: HospitalMedicineBookingDetailsRequest) {
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
    }*/
}