package com.iprism.medrayder.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.medrayder.models.pharmacyaddtocart.PharmacyProductAddToCartApiResponse
import com.iprism.medrayder.models.pharmacyaddtocart.PharmacyProductAddToCartRequest
import com.iprism.medrayder.models.pharmacyproducts.PharmacyProductsApiResponse
import com.iprism.medrayder.models.pharmacyproducts.PharmacyProductsRequest
import com.iprism.medrayder.repository.PharmaciesRepository
import com.iprism.medrayder.utils.UiState
import kotlinx.coroutines.launch

class PharmacyProductsViewModel(private val repository: PharmaciesRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<PharmacyProductsApiResponse>>()
    val response: LiveData<UiState<PharmacyProductsApiResponse>> = _response

    private val _addToCartResponse = MutableLiveData<UiState<PharmacyProductAddToCartApiResponse>>()
    val addToCartResponse: LiveData<UiState<PharmacyProductAddToCartApiResponse>> = _addToCartResponse

    fun fetchPharmacyProducts(request: PharmacyProductsRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.fetchPharmacyProducts(request)
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

    fun addToCartPharmacyProduct(request: PharmacyProductAddToCartRequest) {
        viewModelScope.launch {
            _addToCartResponse.value = UiState.Loading
            try {
                val response = repository.addToCartPharmacyProduct(request)
                if (response.status) {
                    _addToCartResponse.value = UiState.Success(response)
                } else {
                    _addToCartResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _addToCartResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}