package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.onlinedoctorspeacilities.OnlineDoctorSpecialitiesRequest
import com.iprism.swen.models.onlinedoctorspeacilities.SpecialitiesItem
import com.iprism.swen.repository.OnlineDoctorRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class OnlineDoctorSpecialitiesViewModel(private val repository: OnlineDoctorRepository) : ViewModel() {

    private val _specialities = MutableLiveData<UiState<List<SpecialitiesItem>>>()
    val specialities: LiveData<UiState<List<SpecialitiesItem>>> = _specialities


    fun getOnlineDoctorSpecialities(request: OnlineDoctorSpecialitiesRequest) {
        viewModelScope.launch {
            _specialities.value = UiState.Loading
            try {
                val response = repository.getOnlineDoctorSpecialities(request)
                if (response.status) {
                    _specialities.value = UiState.Success(response.onlineDoctorSpecialitiesResponse.specialities)
                } else {
                    _specialities.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _specialities.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}