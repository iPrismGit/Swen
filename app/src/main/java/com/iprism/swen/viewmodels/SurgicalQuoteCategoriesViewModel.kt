package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.allsurgeryquotes.AllSurgeryQuotesApiResponse
import com.iprism.swen.models.allsurgeryquotes.AllSurgeryQuotesRequest
import com.iprism.swen.repository.SurgicalQuoteRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class SurgicalQuoteCategoriesViewModel(private val repository: SurgicalQuoteRepository) : ViewModel()  {

    private val _response = MutableLiveData<UiState<AllSurgeryQuotesApiResponse>>()
    val response: LiveData<UiState<AllSurgeryQuotesApiResponse>> = _response

    fun fetchAllSurgicalQuoteCategories(request: AllSurgeryQuotesRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.fetchAllSurgicalQuoteCategories(request)
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