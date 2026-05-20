package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.hospitalmedicines.HospitalMedicinesApiResponse
import com.iprism.swen.models.hospitalmedicines.HospitalMedicinesRequest
import com.iprism.swen.repository.HospitalsMedicineRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class HospitalMedicineViewModel(private val repository: HospitalsMedicineRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<HospitalMedicinesApiResponse>>()
    val response: LiveData<UiState<HospitalMedicinesApiResponse>> = _response

    fun fetchHospitalMedCategories(request: HospitalMedicinesRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.fetchHospitalMedCategories(request)
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