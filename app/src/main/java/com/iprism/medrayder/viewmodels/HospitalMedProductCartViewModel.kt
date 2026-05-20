package com.iprism.medrayder.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.medrayder.models.bookpharmacyproduct.BookPharmacyProductApiResponse
import com.iprism.medrayder.models.bookpharmacyproduct.BookPharmacyProductRequest
import com.iprism.medrayder.models.hospitalmedicineaddtocart.HospitalMedineProductsAddToCartApiResponse
import com.iprism.medrayder.models.hospitalmedicineaddtocart.HospitalMedineProductsAddToCartRequest
import com.iprism.medrayder.models.hospitalmedicinebooking.HospitalMedicineBookingApiResponse
import com.iprism.medrayder.models.hospitalmedicinebooking.HospitalMedicineBookingRequest
import com.iprism.medrayder.models.hospitalmedicineproductcart.HospitalMedicineProductCartRequest
import com.iprism.medrayder.models.pharmacyaddtocart.PharmacyProductAddToCartApiResponse
import com.iprism.medrayder.models.pharmacyaddtocart.PharmacyProductAddToCartRequest
import com.iprism.medrayder.models.pharmacyproductcart.PharmacyProductCartApiResponse
import com.iprism.medrayder.models.pharmacyproductcart.PharmacyProductCartRequest
import com.iprism.medrayder.repository.HospitalsMedicineRepository
import com.iprism.medrayder.repository.PharmaciesRepository
import com.iprism.medrayder.utils.UiState
import kotlinx.coroutines.launch

class HospitalMedProductCartViewModel(private val repository: HospitalsMedicineRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<PharmacyProductCartApiResponse>>()
    val response: LiveData<UiState<PharmacyProductCartApiResponse>> = _response

    private val _addToCartResponse = MutableLiveData<UiState<HospitalMedineProductsAddToCartApiResponse>>()
    val addToCartResponse: LiveData<UiState<HospitalMedineProductsAddToCartApiResponse>> = _addToCartResponse

    private val _bookingResponse = MutableLiveData<UiState<HospitalMedicineBookingApiResponse>>()
    val bookingResponse: LiveData<UiState<HospitalMedicineBookingApiResponse>> = _bookingResponse

    fun getCart(request: HospitalMedicineProductCartRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.getHospitalMedProductCart(request)
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

    fun bookHospitalMedicine(request: HospitalMedicineBookingRequest) {
        viewModelScope.launch {
            _bookingResponse.value = UiState.Loading
            try {
                val response = repository.bookHospitalMedicineBooking(request)
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