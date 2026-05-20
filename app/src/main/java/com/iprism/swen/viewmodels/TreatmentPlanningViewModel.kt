package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.treatmentplaning.TreatmentPlaningApiResponse
import com.iprism.swen.models.treatmentplaning.TreatmentPlaningRequest
import com.iprism.swen.repository.CommonRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class TreatmentPlanningViewModel(private val repository: CommonRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<TreatmentPlaningApiResponse>>()
    val response: LiveData<UiState<TreatmentPlaningApiResponse>> = _response

    fun treatmentPlaning(request: TreatmentPlaningRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.treatmentPlanning(request)
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