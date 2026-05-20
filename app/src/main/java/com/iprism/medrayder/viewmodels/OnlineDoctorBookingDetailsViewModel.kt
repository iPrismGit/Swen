package com.iprism.medrayder.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.medrayder.models.onlinedoctorbookinghistory.OnlineDoctorBookingHistoryApiResponse
import com.iprism.medrayder.models.onlinedoctorbookinghistory.OnlineDoctorBookingHistoryRequest
import com.iprism.medrayder.models.onlinedoctorrating.OnlineDoctorRatingApiResponse
import com.iprism.medrayder.models.onlinedoctorrating.OnlineDoctorRatingRequest
import com.iprism.medrayder.models.onlinedoctorsinglebookingdetails.OnlineDoctorSingleBookingDetailsApiResponse
import com.iprism.medrayder.models.onlinedoctorsinglebookingdetails.OnlineDoctorSingleBookingDetailsRequest
import com.iprism.medrayder.repository.OnlineDoctorRepository
import com.iprism.medrayder.utils.UiState
import kotlinx.coroutines.launch

class OnlineDoctorBookingDetailsViewModel(private val repository: OnlineDoctorRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<OnlineDoctorSingleBookingDetailsApiResponse>>()
    val response : LiveData<UiState<OnlineDoctorSingleBookingDetailsApiResponse>> = _response

    private val _ratingResponse = MutableLiveData<UiState<OnlineDoctorRatingApiResponse>>()
    val ratingResponse : LiveData<UiState<OnlineDoctorRatingApiResponse>> = _ratingResponse

    fun getOnlineDoctorBookingDetails(request: OnlineDoctorSingleBookingDetailsRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.getOnlineDoctorBookingDetails(request)
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

    fun insertOnlineDoctorRating(request: OnlineDoctorRatingRequest) {
        viewModelScope.launch {
            _ratingResponse.value = UiState.Loading
            try {
                val response = repository.insertOnlineDoctorRating(request)
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