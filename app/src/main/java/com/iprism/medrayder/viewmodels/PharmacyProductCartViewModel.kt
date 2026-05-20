package com.iprism.medrayder.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.medrayder.models.bookpharmacyproduct.BookPharmacyProductApiResponse
import com.iprism.medrayder.models.bookpharmacyproduct.BookPharmacyProductRequest
import com.iprism.medrayder.models.pharmacyaddtocart.PharmacyProductAddToCartApiResponse
import com.iprism.medrayder.models.pharmacyaddtocart.PharmacyProductAddToCartRequest
import com.iprism.medrayder.models.pharmacyproductcart.PharmacyProductCartApiResponse
import com.iprism.medrayder.models.pharmacyproductcart.PharmacyProductCartRequest
import com.iprism.medrayder.repository.PharmaciesRepository
import com.iprism.medrayder.utils.UiState
import kotlinx.coroutines.launch

class PharmacyProductCartViewModel(private val repository: PharmaciesRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<PharmacyProductCartApiResponse>>()
    val response: LiveData<UiState<PharmacyProductCartApiResponse>> = _response

    private val _addToCartResponse = MutableLiveData<UiState<PharmacyProductAddToCartApiResponse>>()
    val addToCartResponse: LiveData<UiState<PharmacyProductAddToCartApiResponse>> = _addToCartResponse

    private val _bookingResponse = MutableLiveData<UiState<BookPharmacyProductApiResponse>>()
    val bookingResponse: LiveData<UiState<BookPharmacyProductApiResponse>> = _bookingResponse

    fun getCart(request: PharmacyProductCartRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.getPharmacyProductCart(request)
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

    fun bookPharmacyProduct(request: BookPharmacyProductRequest) {
        viewModelScope.launch {
            _bookingResponse.value = UiState.Loading
            try {
                val response = repository.bookPharmacyProduct(request)
                if (response.status) {
                    _bookingResponse.value = UiState.Success(response)
                } else {
                    _bookingResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _bookingResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}