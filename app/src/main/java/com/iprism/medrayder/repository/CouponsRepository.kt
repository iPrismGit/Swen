package com.iprism.medrayder.repository

import com.iprism.medrayder.models.onlinedoctorbooking.OnlineDoctorBookingApiResponse
import com.iprism.medrayder.models.onlinedoctorbooking.OnlineDoctorBookingRequest
import com.iprism.medrayder.models.onlinedoctorbookingdetails.OnlineDoctorBookingDetailsApiResponse
import com.iprism.medrayder.models.onlinedoctorbookingdetails.OnlineDoctorBookingDetailsRequest
import com.iprism.medrayder.models.onlinedoctorbookinghistory.OnlineDoctorBookingHistoryApiResponse
import com.iprism.medrayder.models.onlinedoctorbookinghistory.OnlineDoctorBookingHistoryRequest
import com.iprism.medrayder.models.onlinedoctors.OnlineDoctorApiResponse
import com.iprism.medrayder.models.onlinedoctors.OnlineDoctorRequest
import com.iprism.medrayder.models.onlinedoctorscoupons.CouponsApiResponse
import com.iprism.medrayder.models.onlinedoctorscoupons.CouponRequest
import com.iprism.medrayder.models.onlinedoctorsinglebookingdetails.OnlineDoctorSingleBookingDetailsApiResponse
import com.iprism.medrayder.models.onlinedoctorsinglebookingdetails.OnlineDoctorSingleBookingDetailsRequest
import com.iprism.medrayder.models.onlinedoctorspeacilities.OnlineDoctorSpecialitiesApiResponse
import com.iprism.medrayder.models.onlinedoctorspeacilities.OnlineDoctorSpecialitiesRequest
import com.iprism.medrayder.network.MedRayderApi

class CouponsRepository {

    private val apiService = MedRayderApi.medRayderService

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