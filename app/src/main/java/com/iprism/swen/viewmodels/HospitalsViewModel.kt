package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.filters.FiltersApiResponse
import com.iprism.swen.models.filters.FiltersRequest
import com.iprism.swen.models.hospitals.HospitalsApiResponse
import com.iprism.swen.models.hospitals.HospitalsRequest
import com.iprism.swen.repository.HospitalsRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class HospitalsViewModel(private val repository: HospitalsRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<HospitalsApiResponse>>()
    val response: LiveData<UiState<HospitalsApiResponse>> = _response

    private val _filterResponse = MutableLiveData<UiState<FiltersApiResponse>>()
    val filterResponse: LiveData<UiState<FiltersApiResponse>> = _filterResponse

    fun getHospitals(request: HospitalsRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.getHospitals(request)
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

    fun fetchFilters(request: FiltersRequest) {
        viewModelScope.launch {
            _filterResponse.value = UiState.Loading
            try {
                val response = repository.fetchFilters(request)
                if (response.status) {
                    _filterResponse.value = UiState.Success(response)
                } else {
                    _filterResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _filterResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}