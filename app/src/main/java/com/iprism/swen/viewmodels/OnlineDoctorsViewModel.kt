package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.onlinedoctors.OnlineDoctorApiResponse
import com.iprism.swen.models.onlinedoctors.OnlineDoctorRequest
import com.iprism.swen.repository.OnlineDoctorRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class OnlineDoctorsViewModel(private val repository: OnlineDoctorRepository) : ViewModel() {

    private val _doctors = MutableLiveData<UiState<OnlineDoctorApiResponse>>()
    val doctors : LiveData<UiState<OnlineDoctorApiResponse>> = _doctors


    fun getOnlineDoctors(request: OnlineDoctorRequest) {
        viewModelScope.launch {
            _doctors.value = UiState.Loading
            try {
                val response = repository.getOnlineDoctors(request)
                if (response.status) {
                    _doctors.value = UiState.Success(response)
                } else {
                    _doctors.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _doctors.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}