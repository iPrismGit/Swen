package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.symptomdoctorbookingdetails.SymptomDoctorBookingDetailsApiResponse
import com.iprism.swen.models.symptomdoctorbookingdetails.SymptomsDoctorBookingDetailsRequest
import com.iprism.swen.repository.SymptomsDoctorRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class SymptomDoctorTimeSlotViewModel(private val repository: SymptomsDoctorRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<SymptomDoctorBookingDetailsApiResponse>>()
    val response: LiveData<UiState<SymptomDoctorBookingDetailsApiResponse>> = _response

    private val _response1 = MutableLiveData<UiState<SymptomDoctorBookingDetailsApiResponse>>()
    val response1: LiveData<UiState<SymptomDoctorBookingDetailsApiResponse>> = _response1

    fun fetchSymptomDoctorBookingDetails(request: SymptomsDoctorBookingDetailsRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.fetchSymptomDoctorBookingDetails(request)
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

    fun getSlots(request: SymptomsDoctorBookingDetailsRequest) {
        viewModelScope.launch {
            _response1.value = UiState.Loading
            try {
                val response = repository.fetchSymptomDoctorBookingDetails(request)
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