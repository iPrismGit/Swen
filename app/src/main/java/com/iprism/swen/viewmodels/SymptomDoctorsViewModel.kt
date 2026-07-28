package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.symptomsdoctors.SymptomsDoctorsApiResponse
import com.iprism.swen.models.symptomsdoctors.SymptomsDoctorsRequest
import com.iprism.swen.repository.SymptomsDoctorRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class SymptomDoctorsViewModel(private val repository: SymptomsDoctorRepository) : ViewModel()  {

    private val _response = MutableLiveData<UiState<SymptomsDoctorsApiResponse>>()
    val response: LiveData<UiState<SymptomsDoctorsApiResponse>> = _response

    fun fetchSymptomDoctors(request: SymptomsDoctorsRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.fetchAllSymptomDoctors(request)
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