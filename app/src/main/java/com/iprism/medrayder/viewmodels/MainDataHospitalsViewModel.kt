package com.iprism.medrayder.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.medrayder.models.hospitals.HospitalsApiResponse
import com.iprism.medrayder.models.hospitals.HospitalsRequest
import com.iprism.medrayder.models.login.LoginApiResponse
import com.iprism.medrayder.models.login.LoginRequest
import com.iprism.medrayder.models.maindatahospitals.MainDataHospitalsApiResponse
import com.iprism.medrayder.models.maindatahospitals.MainDataHospitalsRequest
import com.iprism.medrayder.models.onlinedoctorbooking.OnlineDoctorBookingApiResponse
import com.iprism.medrayder.models.onlinedoctorbooking.OnlineDoctorBookingRequest
import com.iprism.medrayder.models.onlinedoctorbookingdetails.OnlineDoctorBookingDetailsApiResponse
import com.iprism.medrayder.models.onlinedoctorbookingdetails.OnlineDoctorBookingDetailsRequest
import com.iprism.medrayder.models.resendotp.ResendOtpApiResponse
import com.iprism.medrayder.repository.HospitalsRepository
import com.iprism.medrayder.repository.OnlineDoctorRepository
import com.iprism.medrayder.utils.UiState
import kotlinx.coroutines.launch

class MainDataHospitalsViewModel(private val repository: HospitalsRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<MainDataHospitalsApiResponse>>()
    val response: LiveData<UiState<MainDataHospitalsApiResponse>> = _response

    fun getMainDataHospitals(request: MainDataHospitalsRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.getMainDataHospitals(request)
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