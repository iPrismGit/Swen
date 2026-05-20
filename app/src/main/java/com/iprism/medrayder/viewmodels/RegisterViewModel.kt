package com.iprism.medrayder.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.medrayder.models.dignosticcenters.DiagnosticCentersApiResponse
import com.iprism.medrayder.models.dignosticcenters.DiagnosticCentersRequest
import com.iprism.medrayder.models.homepage.HomePageApiResponse
import com.iprism.medrayder.models.homepage.HomePageRequest
import com.iprism.medrayder.models.labcenters.LabCentersApiResponse
import com.iprism.medrayder.models.labcenters.LabCentersRequest
import com.iprism.medrayder.models.register.RegisterApiResponse
import com.iprism.medrayder.models.register.RegisterRequest
import com.iprism.medrayder.models.userdropdowns.UserDropDownApiResponse
import com.iprism.medrayder.models.userdropdowns.UserDropDownRequest
import com.iprism.medrayder.repository.AuthRepository
import com.iprism.medrayder.repository.CommonRepository
import com.iprism.medrayder.repository.DiagnosticsRepository
import com.iprism.medrayder.repository.LabsRepository
import com.iprism.medrayder.utils.UiState
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<UserDropDownApiResponse>>()
    val response: LiveData<UiState<UserDropDownApiResponse>> = _response

    private val _registerResponse = MutableLiveData<UiState<RegisterApiResponse>>()
    val registerResponse: LiveData<UiState<RegisterApiResponse>> = _registerResponse

    fun fetchUserDropDowns(request: UserDropDownRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.fetchUserDropDowns(request)
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

    fun registerUser(request: RegisterRequest) {
        viewModelScope.launch {
            _registerResponse.value = UiState.Loading
            try {
                val response = repository.registerUser(request)
                if (response.status) {
                    _registerResponse.value = UiState.Success(response)
                } else {
                    _registerResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _registerResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}