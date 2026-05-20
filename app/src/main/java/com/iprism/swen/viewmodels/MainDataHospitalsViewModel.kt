package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.maindatahospitals.MainDataHospitalsApiResponse
import com.iprism.swen.models.maindatahospitals.MainDataHospitalsRequest
import com.iprism.swen.repository.HospitalsRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class MainDataHospitalsViewModel(private val repository: HospitalsRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<MainDataHospitalsApiResponse>>()
    val response: LiveData<UiState<MainDataHospitalsApiResponse>> = _response

    fun getMainDataHospitals(request: MainDataHospitalsRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.getMainDataHospitals(request)
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