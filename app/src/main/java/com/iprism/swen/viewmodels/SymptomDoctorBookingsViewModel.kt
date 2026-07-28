package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.ecmuser.models.doctorrating.DoctorRatingApiResponse
import com.iprism.ecmuser.models.doctorrating.DoctorRatingRequest
import com.iprism.swen.models.symptomdoctorbookings.SymptomDoctorBookingsApiResponse
import com.iprism.swen.models.symptomdoctorbookings.SymptomDoctorBookingsRequest
import com.iprism.swen.models.symptomdoctorsinglebookingdetails.SymptomDoctorSingleBookingDetailsApiResponse
import com.iprism.swen.models.symptomdoctorsinglebookingdetails.SymptomDoctorSingleBookingDetailsRequest
import com.iprism.swen.repository.SymptomsDoctorRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class SymptomDoctorBookingsViewModel(private val repository: SymptomsDoctorRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<
            SymptomDoctorBookingsApiResponse>>()
    val response : LiveData<UiState<SymptomDoctorBookingsApiResponse>> = _response

    private val _completedResponse = MutableLiveData<UiState<SymptomDoctorBookingsApiResponse>>()
    val completedResponse : LiveData<UiState<SymptomDoctorBookingsApiResponse>> = _completedResponse

    private val _bookingDetails = MutableLiveData<UiState<SymptomDoctorSingleBookingDetailsApiResponse>>()
    val bookingDetails : LiveData<UiState<SymptomDoctorSingleBookingDetailsApiResponse>> = _bookingDetails

    private val _ratingResponse = MutableLiveData<UiState<DoctorRatingApiResponse>>()
    val ratingResponse : LiveData<UiState<DoctorRatingApiResponse>> = _ratingResponse


    fun fetchSymptomDoctorsOngoingBookings(request: SymptomDoctorBookingsRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.fetchSymptomDoctorOngoingBookings(request)
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

    fun fetchSymptomDoctorsCompletedBookings(request: SymptomDoctorBookingsRequest) {
        viewModelScope.launch {
            _completedResponse.value = UiState.Loading
            try {
                val response = repository.fetchSymptomDoctorCompletedBookings(request)
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

    fun fetchSymptomDoctorBookingDetails(request: SymptomDoctorSingleBookingDetailsRequest) {
        viewModelScope.launch {
            _bookingDetails.value = UiState.Loading
            try {
                val response = repository.fetchSymptomDoctorSingleBookingDetails(request)
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

    fun insertDoctorRating(request: DoctorRatingRequest) {
        viewModelScope.launch {
            _ratingResponse.value = UiState.Loading
            try {
                val response = repository.insertDoctorRating(request)
                if (response.status) {
                    _ratingResponse.value = UiState.Success(response)
                } else {
                    _ratingResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _ratingResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}