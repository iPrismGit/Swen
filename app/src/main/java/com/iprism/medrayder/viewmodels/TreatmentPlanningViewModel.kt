package com.iprism.medrayder.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.medrayder.models.address.AddAddressApiResponse
import com.iprism.medrayder.models.address.AddAddressRequest
import com.iprism.medrayder.models.dignosticcenters.DiagnosticCentersApiResponse
import com.iprism.medrayder.models.dignosticcenters.DiagnosticCentersRequest
import com.iprism.medrayder.models.homepage.HomePageApiResponse
import com.iprism.medrayder.models.homepage.HomePageRequest
import com.iprism.medrayder.models.labcenters.LabCentersApiResponse
import com.iprism.medrayder.models.labcenters.LabCentersRequest
import com.iprism.medrayder.models.medlocker.MedLockerApiResponse
import com.iprism.medrayder.models.medlocker.MedLockerRequest
import com.iprism.medrayder.models.treatmentplaning.TreatmentPlaningApiResponse
import com.iprism.medrayder.models.treatmentplaning.TreatmentPlaningRequest
import com.iprism.medrayder.repository.AddressRepository
import com.iprism.medrayder.repository.CommonRepository
import com.iprism.medrayder.repository.DiagnosticsRepository
import com.iprism.medrayder.repository.LabsRepository
import com.iprism.medrayder.utils.UiState
import kotlinx.coroutines.launch

class TreatmentPlanningViewModel(private val repository: CommonRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<TreatmentPlaningApiResponse>>()
    val response: LiveData<UiState<TreatmentPlaningApiResponse>> = _response

    fun treatmentPlaning(request: TreatmentPlaningRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.treatmentPlanning(request)
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