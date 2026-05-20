package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.hospitalmedicineaddtocart.HospitalMedineProductsAddToCartApiResponse
import com.iprism.swen.models.hospitalmedicineaddtocart.HospitalMedineProductsAddToCartRequest
import com.iprism.swen.models.hospitalmedicineproducts.HospitalMedicineProductsApiResponse
import com.iprism.swen.models.hospitalmedicineproducts.HospitalMedicineProductsRequest
import com.iprism.swen.repository.HospitalsMedicineRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class HospitalMedicineProductsViewModel(private val repository: HospitalsMedicineRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<HospitalMedicineProductsApiResponse>>()
    val response: LiveData<UiState<HospitalMedicineProductsApiResponse>> = _response

    private val _addToCartResponse = MutableLiveData<UiState<HospitalMedineProductsAddToCartApiResponse>>()
    val addToCartResponse: LiveData<UiState<HospitalMedineProductsAddToCartApiResponse>> = _addToCartResponse

    fun fetchHospitalMedicineProducts(request: HospitalMedicineProductsRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.fetchHospitalMedProducts(request)
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

    fun addToCartProduct(request: HospitalMedineProductsAddToCartRequest) {
        viewModelScope.launch {
            _addToCartResponse.value = UiState.Loading
            try {
                val response = repository.addToCartProduct(request)
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