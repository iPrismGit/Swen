package com.iprism.swen.repository

import com.iprism.swen.models.onlinedoctorscoupons.CouponsApiResponse
import com.iprism.swen.models.onlinedoctorscoupons.CouponRequest
import com.iprism.swen.network.SwenAPi

class CouponsRepository {

    private val apiService = SwenAPi.swenApiService

    suspend fun getOnlineDoctorsCoupons(request: CouponRequest): CouponsApiResponse {
        return apiService.getOnlineDoctorsCoupons(request)
    }

    suspend fun getPharmacyCoupons(request: CouponRequest): CouponsApiResponse {
        return apiService.fetchPharmacyCoupons(request)
    }

    suspend fun fetchDiagnosticCoupons(request: CouponRequest): CouponsApiResponse {
        return apiService.fetchDiagnosticCoupons(request)
    }

    suspend fun fetchLabCoupons(request: CouponRequest): CouponsApiResponse {
        return apiService.fetchLabCoupons(request)
    }

    suspend fun fetchMedicineCoupons(request: CouponRequest): CouponsApiResponse {
        return apiService.getHospitalMedicineCoupons(request)
    }


    suspend fun fetchHospitalDiagnosticCoupons(request: CouponRequest): CouponsApiResponse {
        return apiService.fetchHospitalDiagnosticCoupons(request)
    }
}