package com.iprism.swen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.swen.models.onlinedoctorscoupons.CouponsItem
import com.iprism.swen.models.onlinedoctorscoupons.CouponRequest
import com.iprism.swen.repository.HospitalsRepository
import com.iprism.swen.utils.UiState
import kotlinx.coroutines.launch

class HospitalDoctorCouponsViewModel(private val repository: HospitalsRepository) : ViewModel() {

    private val _coupons = MutableLiveData<UiState<List<CouponsItem>>>()
    val coupons : LiveData<UiState<List<CouponsItem>>> = _coupons

    fun getOnlineCoupons(request: CouponRequest) {
        viewModelScope.launch {
            _coupons.value = UiState.Loading
            try {
                val response = repository.getHospitalDoctorCoupons(request)
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