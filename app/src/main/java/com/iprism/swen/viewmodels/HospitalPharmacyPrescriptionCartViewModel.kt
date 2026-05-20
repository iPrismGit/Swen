package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.hospitalpharmacyprescriptionbooking.HospitalPharmacyPrescriptionBookingApiResponse
import com.iprism.swen.models.hospitalpharmacyprescriptionbooking.HospitalPharmacyPrescriptionBookingRequest
import com.iprism.swen.repository.HospitalsMedicineRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class HospitalPharmacyPrescriptionCartViewModel(private val repository: HospitalsMedicineRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<HospitalPharmacyPrescriptionBookingApiResponse>>()
    val response: LiveData<UiState<HospitalPharmacyPrescriptionBookingApiResponse>> = _response

    private val _bookingResponse = MutableLiveData<UiState<HospitalPharmacyPrescriptionBookingApiResponse>>()
    val bookingResponse: LiveData<UiState<HospitalPharmacyPrescriptionBookingApiResponse>> = _bookingResponse

    fun viewHospitalPharmacyPrescriptionOrder(request: HospitalPharmacyPrescriptionBookingRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.bookHospitalMedPrescription(request)
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

    fun bookHospitalPharmacyPrescriptionOrder(request: HospitalPharmacyPrescriptionBookingRequest) {
        viewModelScope.launch {
            _bookingResponse.value = UiState.Loading
            try {
                val response = repository.bookHospitalMedPrescription(request)
                if (response.status) {
                    _bookingResponse.value = UiState.Success(response)
                } else {
                    _bookingResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _bookingResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}