package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.register.RegisterApiResponse
import com.iprism.swen.models.register.RegisterRequest
import com.iprism.swen.models.userdropdowns.UserDropDownApiResponse
import com.iprism.swen.models.userdropdowns.UserDropDownRequest
import com.iprism.swen.repository.AuthRepository
import com.iprism.swen.utils.UiState
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