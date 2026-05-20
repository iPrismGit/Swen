package com.iprism.medrayder.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.medrayder.models.hospitaldoctortimeslots.HospitalDoctorTimeSlotsRequest
import com.iprism.medrayder.models.onlinedoctorbookingdetails.OnlineDoctorBookingDetailsApiResponse
import com.iprism.medrayder.repository.HospitalsRepository
import com.iprism.medrayder.utils.UiState
import kotlinx.coroutines.launch

class HospitalDoctorTimeSlotViewModel(private val repository: HospitalsRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<OnlineDoctorBookingDetailsApiResponse>>()
    val response: LiveData<UiState<OnlineDoctorBookingDetailsApiResponse>> = _response

    private val _response1 = MutableLiveData<UiState<OnlineDoctorBookingDetailsApiResponse>>()
    val response1: LiveData<UiState<OnlineDoctorBookingDetailsApiResponse>> = _response1

    fun getHospitalDoctorBookingDetails(request: HospitalDoctorTimeSlotsRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.getHospitalDoctorTimeSlots(request)
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

    fun getSlots(request: HospitalDoctorTimeSlotsRequest) {
        viewModelScope.launch {
            _response1.value = UiState.Loading
            try {
                val response = repository.getHospitalDoctorTimeSlots(request)
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