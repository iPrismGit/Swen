package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.healthmedia.HealthMediaRequest
import com.iprism.swen.models.diettips.DietTipsApiResponse
import com.iprism.swen.repository.CommonRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class DietTipsViewModel(private val repository: CommonRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<DietTipsApiResponse>>()
    val response: LiveData<UiState<DietTipsApiResponse>> = _response

    fun fetchDietTips(request: HealthMediaRequest) {
        _response.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = repository.fetchDietTips(request)
                if (response.status) {
                    _response.value = UiState.Success(response)
                } else {
                    _response.value = UiState.Error(response.message)
                }
            } catch (e: Exception) {
                _response.value = UiState.Error(e.message ?: "Something went wrong")
            }
        }
    }
}