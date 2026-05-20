package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.hospitalambulancebookings.HospitalAmbulanceBookingApiResponse
import com.iprism.swen.models.labtestbookings.LabTestBookingsRequest
import com.iprism.swen.repository.HospitalsRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class HospitalAmbulanceBookingsViewModel(private val repository: HospitalsRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<HospitalAmbulanceBookingApiResponse>>()
    val response : LiveData<UiState<HospitalAmbulanceBookingApiResponse>> = _response

    private val _completedResponse = MutableLiveData<UiState<HospitalAmbulanceBookingApiResponse>>()
    val completedResponse : LiveData<UiState<HospitalAmbulanceBookingApiResponse>> = _completedResponse



    fun fetchHospitalAmbulanceOngoingBookings(request: LabTestBookingsRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.fetchHospitalAmbulanceOngoingBookings(request)
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

    fun fetchHospitalAmbulanceCompletedBookings(request: LabTestBookingsRequest) {
        viewModelScope.launch {
            _completedResponse.value = UiState.Loading
            try {
                val response = repository.fetchHospitalAmbulanceCompletedBookings(request)
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