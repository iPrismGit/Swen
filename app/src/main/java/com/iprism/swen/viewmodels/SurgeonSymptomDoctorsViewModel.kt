package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.surgeonsymptomdoctors.SurgenSymptomDoctorsApiResponse
import com.iprism.swen.models.surgeonsymptomdoctors.SurgeonSymptomDoctorsRequest
import com.iprism.swen.repository.SurgeonSymptomsDoctorRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class SurgeonSymptomDoctorsViewModel(private val repository: SurgeonSymptomsDoctorRepository) : ViewModel()  {

    private val _response = MutableLiveData<UiState<
            SurgenSymptomDoctorsApiResponse>>()
    val response: LiveData<UiState<SurgenSymptomDoctorsApiResponse>> = _response

    fun fetchSurgeonSymptomDoctors(request: SurgeonSymptomDoctorsRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.fetchSurgeonSymptomDoctors(request)
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