package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.notifications.NotificationsApiResponse
import com.iprism.swen.models.notifications.NotificationsRequest
import com.iprism.swen.repository.CommonRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class NotificationsViewModel(private val repository: CommonRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<NotificationsApiResponse>>()
    val response: LiveData<UiState<NotificationsApiResponse>> = _response

    fun fetchNotifications(request: NotificationsRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.fetchNotifications(request)
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