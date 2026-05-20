package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.hospitaldetails.HospitalDetailsApiResponse
import com.iprism.swen.models.hospitaldetails.HospitalDetailsRequest
import com.iprism.swen.repository.HospitalsRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class HospitalDetailsViewModel(private val repository: HospitalsRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<HospitalDetailsApiResponse>>()
    val response: LiveData<UiState<HospitalDetailsApiResponse>> = _response

    fun getHospitalDetails(request: HospitalDetailsRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.getHospitalDetails(request)
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