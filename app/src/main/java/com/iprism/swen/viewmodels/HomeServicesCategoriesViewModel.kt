package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.homeservices.HomeServicesApiResponse
import com.iprism.swen.models.homeservices.HomeServicesRequest
import com.iprism.swen.repository.HomeVisitServicesRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class HomeServicesCategoriesViewModel(private val repository: HomeVisitServicesRepository) : ViewModel()  {

    private val _response = MutableLiveData<UiState<HomeServicesApiResponse>>()
    val response: LiveData<UiState<HomeServicesApiResponse>> = _response

    fun fetchHomeServices(request: HomeServicesRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.fetchHomeVisitServices(request)
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