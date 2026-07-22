package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.surgeondoctorbookingdetails.SurgeonDoctorBookingDetailsApiResponse
import com.iprism.swen.models.surgeondoctorbookingdetails.SurgeonDoctorBookingDetailsRequest
import com.iprism.swen.repository.SurgeonSymptomsDoctorRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class SurgeonDoctorTimeSlotViewModel(private val repository: SurgeonSymptomsDoctorRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<SurgeonDoctorBookingDetailsApiResponse>>()
    val response: LiveData<UiState<SurgeonDoctorBookingDetailsApiResponse>> = _response

    private val _response1 = MutableLiveData<UiState<SurgeonDoctorBookingDetailsApiResponse>>()
    val response1: LiveData<UiState<SurgeonDoctorBookingDetailsApiResponse>> = _response1

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

    fun getSlots(request: SurgeonDoctorBookingDetailsRequest) {
        viewModelScope.launch {
            _response1.value = UiState.Loading
            try {
                val response = repository.fetchSurgeonDoctorBookingDetails(request)
                if (response.status) {
                    _response1.value = UiState.Success(response)
                } else {
                    _response1.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _response1.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}