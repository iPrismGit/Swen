package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.onlinedoctorscoupons.CouponsItem
import com.iprism.swen.models.onlinedoctorscoupons.CouponRequest
import com.iprism.swen.repository.CouponsRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class CouponsViewModel(private val repository: CouponsRepository) : ViewModel() {

    private val _coupons = MutableLiveData<UiState<List<CouponsItem>>>()
    val coupons : LiveData<UiState<List<CouponsItem>>> = _coupons

    fun getOnlineCoupons(request: CouponRequest) {
        viewModelScope.launch {
            _coupons.value = UiState.Loading
            try {
                val response = repository.getOnlineDoctorsCoupons(request)
                if (response.status) {
                    _coupons.value = UiState.Success(response.couponsResponse.coupons)
                } else {
                    _coupons.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _coupons.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun getPharmacyCoupons(request: CouponRequest) {
        viewModelScope.launch {
            _coupons.value = UiState.Loading
            try {
                val response = repository.getPharmacyCoupons(request)
                if (response.status) {
                    _coupons.value = UiState.Success(response.couponsResponse.coupons)
                } else {
                    _coupons.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _coupons.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun fetchDiagnosticCoupons(request: CouponRequest) {
        viewModelScope.launch {
            _coupons.value = UiState.Loading
            try {
                val response = repository.fetchDiagnosticCoupons(request)
                if (response.status) {
                    _coupons.value = UiState.Success(response.couponsResponse.coupons)
                } else {
                    _coupons.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _coupons.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun fetchLabCoupons(request: CouponRequest) {
        viewModelScope.launch {
            _coupons.value = UiState.Loading
            try {
                val response = repository.fetchLabCoupons(request)
                if (response.status) {
                    _coupons.value = UiState.Success(response.couponsResponse.coupons)
                } else {
                    _coupons.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _coupons.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun fetchMedicineCoupons(request: CouponRequest) {
        viewModelScope.launch {
            _coupons.value = UiState.Loading
            try {
                val response = repository.fetchMedicineCoupons(request)
                if (response.status) {
                    _coupons.value = UiState.Success(response.couponsResponse.coupons)
                } else {
                    _coupons.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _coupons.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun fetchHospitalDiagnosticCoupons(request: CouponRequest) {
        viewModelScope.launch {
            _coupons.value = UiState.Loading
            try {
                val response = repository.fetchHospitalDiagnosticCoupons(request)
                if (response.status) {
                    _coupons.value = UiState.Success(response.couponsResponse.coupons)
                } else {
                    _coupons.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _coupons.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}