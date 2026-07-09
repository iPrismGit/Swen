package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.homeservicesbooking.HomeServicesBookingApiResponse
import com.iprism.swen.models.homeservicesbooking.HomeServicesBookingRequest
import com.iprism.swen.repository.HomeVisitServicesRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class HomeServicesBookingViewModel(private val repository: HomeVisitServicesRepository) : ViewModel()  {

    private val _response = MutableLiveData<UiState<HomeServicesBookingApiResponse>>()
    val response: LiveData<UiState<HomeServicesBookingApiResponse>> = _response

    fun bookHomeServices(request: HomeServicesBookingRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.bookHomeVisitService(request)
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