package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.login.LoginApiResponse
import com.iprism.swen.models.login.LoginRequest
import com.iprism.swen.models.resendotp.ResendOtpApiResponse
import com.iprism.swen.models.resendotp.ResendOtpRequest
import com.iprism.swen.repository.AuthRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _loginResponse = MutableLiveData<UiState<LoginApiResponse>>()
    val loginResponse: LiveData<UiState<LoginApiResponse>> = _loginResponse

    private val _resendResponse = MutableLiveData<UiState<ResendOtpApiResponse>>()
    val resendResponse: LiveData<UiState<ResendOtpApiResponse>> = _resendResponse

    fun login(request: LoginRequest) {
        viewModelScope.launch {
            _loginResponse.value = UiState.Loading
            try {
                val response = repository.login(request)
                if (response.status) {
                    _loginResponse.value = UiState.Success(response)
                } else {
                    _loginResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _loginResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun resendOtp(request: ResendOtpRequest) {
        viewModelScope.launch {
            _resendResponse.value = UiState.Loading
            try {
                val response = repository.resendOtp(request)
                if (response.status) {
                    _resendResponse.value = UiState.Success(response)
                } else {
                    _resendResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _resendResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}