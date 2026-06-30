package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.insertsurgicalquote.InsertSurgicalQuoteApiResponse
import com.iprism.swen.models.insertsurgicalquote.InsertSurgicalQuoteRequest
import com.iprism.swen.repository.SurgicalQuoteRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class InsertSurgicalQuoteViewModel(private val repository: SurgicalQuoteRepository) : ViewModel()  {

    private val _response = MutableLiveData<UiState<InsertSurgicalQuoteApiResponse>>()
    val response: LiveData<UiState<InsertSurgicalQuoteApiResponse>> = _response

    fun insertSurgicalQuote(request: InsertSurgicalQuoteRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.insertSurgicalQuote(request)
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