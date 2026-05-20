package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.onlinedoctorbooking.OnlineDoctorBookingApiResponse
import com.iprism.swen.models.onlinedoctorbooking.OnlineDoctorBookingRequest
import com.iprism.swen.repository.OnlineDoctorRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class DoctorSummaryViewModel(private val repository: OnlineDoctorRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<OnlineDoctorBookingApiResponse>>()
    val response: LiveData<UiState<OnlineDoctorBookingApiResponse>> = _response

    fun bookOnlineDoctor(request: OnlineDoctorBookingRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.bookOnlineDoctor(request)
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
}