package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.surgeondoctorbooking.SurgeonDoctorBookingApiResponse
import com.iprism.swen.models.surgeondoctorbooking.SurgeonDoctorBookingRequest
import com.iprism.swen.models.surgeondoctorbookingdetails.SurgeonDoctorBookingDetailsApiResponse
import com.iprism.swen.models.surgeondoctorbookingdetails.SurgeonDoctorBookingDetailsRequest
import com.iprism.swen.repository.SurgeonSymptomsDoctorRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class SurgeonDoctorSummaryViewModel(private val repository: SurgeonSymptomsDoctorRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<SurgeonDoctorBookingDetailsApiResponse>>()
    val response: LiveData<UiState<SurgeonDoctorBookingDetailsApiResponse>> = _response

    private val _bookingResponse = MutableLiveData<UiState<SurgeonDoctorBookingApiResponse>>()
    val bookingResponse: LiveData<UiState<SurgeonDoctorBookingApiResponse>> = _bookingResponse

    fun fetchSurgeonDoctorBookingDetails(request: SurgeonDoctorBookingDetailsRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.fetchSurgeonDoctorBookingDetails(request)
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

    fun bookSurgeonDoctor(request: SurgeonDoctorBookingRequest) {
        viewModelScope.launch {
            _bookingResponse.value = UiState.Loading
            try {
                val response = repository.bookSurgeonDoctor(request)
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