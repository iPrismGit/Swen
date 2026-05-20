package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.onlinedoctorbookinghistory.OnlineDoctorBookingHistoryApiResponse
import com.iprism.swen.models.onlinedoctorbookinghistory.OnlineDoctorBookingHistoryRequest
import com.iprism.swen.repository.HospitalsRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class HospitalDoctorBookingsViewModel(private val repository: HospitalsRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<OnlineDoctorBookingHistoryApiResponse>>()
    val response : LiveData<UiState<OnlineDoctorBookingHistoryApiResponse>> = _response

    private val _completedResponse = MutableLiveData<UiState<OnlineDoctorBookingHistoryApiResponse>>()
    val completedResponse : LiveData<UiState<OnlineDoctorBookingHistoryApiResponse>> = _completedResponse


    fun getHospitalDoctorBookingsHistory(request: OnlineDoctorBookingHistoryRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.getHospitalDoctorBookingsHistory(request)
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

    fun getHospitalDoctorCompletedBookings(request: OnlineDoctorBookingHistoryRequest) {
        viewModelScope.launch {
            _completedResponse.value = UiState.Loading
            try {
                val response = repository.getHospitalDoctorCompletedBookings(request)
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
}