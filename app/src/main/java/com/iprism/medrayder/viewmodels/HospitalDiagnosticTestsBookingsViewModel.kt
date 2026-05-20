package com.iprism.medrayder.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.medrayder.models.diagnostictestbookings.DiagnosticTestsBookingsApiResponse
import com.iprism.medrayder.models.hospitaladmitongoingbookings.HospitalAdmissionOnGoingApiResponse
import com.iprism.medrayder.models.hospitaladmitongoingbookings.HospitalAdmissionOnGoingRequest
import com.iprism.medrayder.models.hospitaldiagnosticbookingdetails.HospitalDiagnosticTestBookingDetailsApiResponse
import com.iprism.medrayder.models.hospitaldiagnosticbookingdetails.HospitalDiagnosticTestBookingDetailsRequest
import com.iprism.medrayder.models.hospitalmedbookingdetails.HospitalMedicineBookingDetailsApiResponse
import com.iprism.medrayder.models.hospitalmedbookingdetails.HospitalMedicineBookingDetailsRequest
import com.iprism.medrayder.models.hospitalmedicneongoing.HospitalMedicineOngoingBookingApiResponse
import com.iprism.medrayder.models.hospitalmedicneongoing.HospitalMedicineOngoingBookingRequest
import com.iprism.medrayder.models.labtestbookings.LabTestBookingsApiResponse
import com.iprism.medrayder.models.labtestbookings.LabTestBookingsRequest
import com.iprism.medrayder.repository.DiagnosticsRepository
import com.iprism.medrayder.repository.HospitalDiagnosticsRepository
import com.iprism.medrayder.repository.HospitalsMedicineRepository
import com.iprism.medrayder.repository.HospitalsRepository
import com.iprism.medrayder.repository.LabsRepository
import com.iprism.medrayder.utils.UiState
import kotlinx.coroutines.launch

class HospitalDiagnosticTestsBookingsViewModel(private val repository: HospitalDiagnosticsRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<DiagnosticTestsBookingsApiResponse>>()
    val response : LiveData<UiState<DiagnosticTestsBookingsApiResponse>> = _response

    private val _completedResponse = MutableLiveData<UiState<DiagnosticTestsBookingsApiResponse>>()
    val completedResponse : LiveData<UiState<DiagnosticTestsBookingsApiResponse>> = _completedResponse

    private val _bookingDetails = MutableLiveData<UiState<HospitalDiagnosticTestBookingDetailsApiResponse>>()
    val bookingDetails : LiveData<UiState<HospitalDiagnosticTestBookingDetailsApiResponse>> = _bookingDetails


    fun fetchHospitalDiagnosticTestsOngoingBookings(request: LabTestBookingsRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.fetchHospitalDiagnosticTestsOngoingBookings(request)
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

    fun fetchHospitalDiagnosticTestsCompletedBookings(request: LabTestBookingsRequest) {
        viewModelScope.launch {
            _completedResponse.value = UiState.Loading
            try {
                val response = repository.fetchHospitalDiagnosticTestsCompletedBookings(request)
                if (response.status) {
                    _completedResponse.value = UiState.Success(response)
                } else {
                    _completedResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _completedResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun fetchHospitalDiagnosticBookingDetails(request: HospitalDiagnosticTestBookingDetailsRequest) {
        viewModelScope.launch {
            _bookingDetails.value = UiState.Loading
            try {
                val response = repository.fetchHospitalDiagnosticTestBookingDetails(request)
                if (response.status) {
                    _bookingDetails.value = UiState.Success(response)
                } else {
                    _bookingDetails.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _bookingDetails.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}