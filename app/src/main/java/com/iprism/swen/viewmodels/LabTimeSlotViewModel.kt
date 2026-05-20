package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.labtestslots.LabTestSlotsApiResponse
import com.iprism.swen.models.labtestslots.LabTestSlotsRequest
import com.iprism.swen.repository.LabsRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class LabTimeSlotViewModel(private val repository: LabsRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<LabTestSlotsApiResponse>>()
    val response: LiveData<UiState<LabTestSlotsApiResponse>> = _response

    private val _response1 = MutableLiveData<UiState<LabTestSlotsApiResponse>>()
    val response1: LiveData<UiState<LabTestSlotsApiResponse>> = _response1

    fun getDates(request: LabTestSlotsRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.fetchLabTestBookingDetails(request)
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

    fun getSlots(request: LabTestSlotsRequest) {
        viewModelScope.launch {
            _response1.value = UiState.Loading
            try {
                val response = repository.fetchLabTestBookingDetails(request)
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