package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.symptomdoctorbooking.SymptomDoctorBookingApiResponse
import com.iprism.swen.models.symptomdoctorbooking.SymptomDoctorBookingRequest
import com.iprism.swen.models.symptomdoctorbookingdetails.SymptomDoctorBookingDetailsApiResponse
import com.iprism.swen.models.symptomdoctorbookingdetails.SymptomsDoctorBookingDetailsRequest
import com.iprism.swen.repository.SymptomsDoctorRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class SymptomDoctorSummaryViewModel(private val repository: SymptomsDoctorRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<SymptomDoctorBookingDetailsApiResponse>>()
    val response: LiveData<UiState<SymptomDoctorBookingDetailsApiResponse>> = _response

    private val _bookingResponse = MutableLiveData<UiState<SymptomDoctorBookingApiResponse>>()
    val bookingResponse: LiveData<UiState<SymptomDoctorBookingApiResponse>> = _bookingResponse

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

    fun bookSymptomDoctor(request: SymptomDoctorBookingRequest) {
        viewModelScope.launch {
            _bookingResponse.value = UiState.Loading
            try {
                val response = repository.bookSymptomDoctor(request)
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