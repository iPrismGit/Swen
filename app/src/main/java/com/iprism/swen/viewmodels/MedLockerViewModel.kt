package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.medlocker.MedLockerApiResponse
import com.iprism.swen.models.medlocker.MedLockerRequest
import com.iprism.swen.repository.CommonRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class MedLockerViewModel(private val repository: CommonRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<MedLockerApiResponse>>()
    val response: LiveData<UiState<MedLockerApiResponse>> = _response

    fun medLocker(request: MedLockerRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.medLocker(request)
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