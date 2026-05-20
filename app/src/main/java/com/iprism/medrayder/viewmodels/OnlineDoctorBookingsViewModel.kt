package com.iprism.medrayder.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.medrayder.models.onlinedoctorbookinghistory.OnlineDoctorBookingHistoryApiResponse
import com.iprism.medrayder.models.onlinedoctorbookinghistory.OnlineDoctorBookingHistoryRequest
import com.iprism.medrayder.repository.OnlineDoctorRepository
import com.iprism.medrayder.utils.UiState
import kotlinx.coroutines.launch

class OnlineDoctorBookingsViewModel(private val repository: OnlineDoctorRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<OnlineDoctorBookingHistoryApiResponse>>()
    val response : LiveData<UiState<OnlineDoctorBookingHistoryApiResponse>> = _response

    private val _completedResponse = MutableLiveData<UiState<OnlineDoctorBookingHistoryApiResponse>>()
    val completedResponse : LiveData<UiState<OnlineDoctorBookingHistoryApiResponse>> = _completedResponse


    fun getOnlineDoctorBookings(request: OnlineDoctorBookingHistoryRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.getOnlineDoctorBookings(request)
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

    fun getOnlineDoctorCompletedBookings(request: OnlineDoctorBookingHistoryRequest) {
        viewModelScope.launch {
            _completedResponse.value = UiState.Loading
            try {
                val response = repository.getOnlineDoctorCompletedBookings(request)
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