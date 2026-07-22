package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.ecmuser.models.doctorrating.DoctorRatingApiResponse
import com.iprism.ecmuser.models.doctorrating.DoctorRatingRequest
import com.iprism.swen.models.surgeondoctorbookings.SurgeonDoctorBookingsApiResponse
import com.iprism.swen.models.surgeondoctorbookings.SurgeonDoctorBookingsRequest
import com.iprism.swen.models.surgeondoctorsinglebookingdetails.SurgeonDoctorSingleBookingDetailsApiResponse
import com.iprism.swen.models.surgeondoctorsinglebookingdetails.SurgeonDoctorSingleBookingDetailsRequest
import com.iprism.swen.repository.SurgeonSymptomsDoctorRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class SurgeonDoctorBookingsViewModel(private val repository: SurgeonSymptomsDoctorRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<SurgeonDoctorBookingsApiResponse>>()
    val response : LiveData<UiState<SurgeonDoctorBookingsApiResponse>> = _response

    private val _completedResponse = MutableLiveData<UiState<SurgeonDoctorBookingsApiResponse>>()
    val completedResponse : LiveData<UiState<SurgeonDoctorBookingsApiResponse>> = _completedResponse

    private val _bookingDetails = MutableLiveData<UiState<SurgeonDoctorSingleBookingDetailsApiResponse>>()
    val bookingDetails : LiveData<UiState<SurgeonDoctorSingleBookingDetailsApiResponse>> = _bookingDetails

    private val _ratingResponse = MutableLiveData<UiState<DoctorRatingApiResponse>>()
    val ratingResponse : LiveData<UiState<DoctorRatingApiResponse>> = _ratingResponse


    fun fetchSurgeonDoctorsOngoingBookings(request: SurgeonDoctorBookingsRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.fetchSurgeonDoctorOngoingBookings(request)
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

    fun fetchSurgeonDoctorsCompletedBookings(request: SurgeonDoctorBookingsRequest) {
        viewModelScope.launch {
            _completedResponse.value = UiState.Loading
            try {
                val response = repository.fetchSurgeonDoctorCompletedBookings(request)
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

    fun fetchSurgeonDoctorBookingDetails(request: SurgeonDoctorSingleBookingDetailsRequest) {
        viewModelScope.launch {
            _bookingDetails.value = UiState.Loading
            try {
                val response = repository.fetchSurgeonDoctorSingleBookingDetails(request)
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