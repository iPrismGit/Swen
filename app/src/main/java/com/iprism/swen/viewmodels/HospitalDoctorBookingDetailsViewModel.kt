package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.onlinedoctorrating.OnlineDoctorRatingApiResponse
import com.iprism.swen.models.onlinedoctorrating.OnlineDoctorRatingRequest
import com.iprism.swen.models.onlinedoctorsinglebookingdetails.OnlineDoctorSingleBookingDetailsApiResponse
import com.iprism.swen.models.onlinedoctorsinglebookingdetails.OnlineDoctorSingleBookingDetailsRequest
import com.iprism.swen.repository.HospitalsRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class HospitalDoctorBookingDetailsViewModel(private val repository: HospitalsRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<OnlineDoctorSingleBookingDetailsApiResponse>>()
    val response : LiveData<UiState<OnlineDoctorSingleBookingDetailsApiResponse>> = _response

    private val _ratingResponse = MutableLiveData<UiState<OnlineDoctorRatingApiResponse>>()
    val ratingResponse : LiveData<UiState<OnlineDoctorRatingApiResponse>> = _ratingResponse

    fun getHospitalDoctorSingleBookingDetails(request: OnlineDoctorSingleBookingDetailsRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.getHospitalDoctorSingleBookingDetails(request)
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

    fun insertHospitalDoctorRating(request: OnlineDoctorRatingRequest) {
        viewModelScope.launch {
            _ratingResponse.value = UiState.Loading
            try {
                val response = repository.insertHospitalDoctorRating(request)
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