package com.iprism.medrayder.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.medrayder.models.login.LoginRequest
import com.iprism.medrayder.models.onlinedoctorspeacilities.OnlineDoctorSpecialitiesRequest
import com.iprism.medrayder.models.onlinedoctorspeacilities.SpecialitiesItem
import com.iprism.medrayder.repository.OnlineDoctorRepository
import com.iprism.medrayder.utils.UiState
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