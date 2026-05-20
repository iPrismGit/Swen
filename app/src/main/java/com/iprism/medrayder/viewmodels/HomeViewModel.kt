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
import com.iprism.medrayder.models.notifications.NotificationsApiResponse
import com.iprism.medrayder.models.notifications.NotificationsRequest
import com.iprism.medrayder.repository.CommonRepository
import com.iprism.medrayder.repository.DiagnosticsRepository
import com.iprism.medrayder.repository.LabsRepository
import com.iprism.medrayder.utils.UiState
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: CommonRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<HomePageApiResponse>>()
    val response: LiveData<UiState<HomePageApiResponse>> = _response

    private val _notificationCountResponse = MutableLiveData<UiState<NotificationsApiResponse>>()
    val notificationCountResponse: LiveData<UiState<NotificationsApiResponse>> = _notificationCountResponse

    fun fetchHomePage(request: HomePageRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.fetchHomePage(request)
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

    fun fetchNotifications(request: NotificationsRequest) {
        viewModelScope.launch {
            _notificationCountResponse.value = UiState.Loading
            try {
                val response = repository.fetchNotifications(request)
                if (response.status) {
                    _notificationCountResponse.value = UiState.Success(response)
                } else {
                    _notificationCountResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _notificationCountResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}