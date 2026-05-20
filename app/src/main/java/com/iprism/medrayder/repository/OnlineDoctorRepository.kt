package com.iprism.medrayder.repository

import com.iprism.medrayder.models.onlinedoctorbooking.OnlineDoctorBookingApiResponse
import com.iprism.medrayder.models.onlinedoctorbooking.OnlineDoctorBookingRequest
import com.iprism.medrayder.models.onlinedoctorbookingdetails.OnlineDoctorBookingDetailsApiResponse
import com.iprism.medrayder.models.onlinedoctorbookingdetails.OnlineDoctorBookingDetailsRequest
import com.iprism.medrayder.models.onlinedoctorbookinghistory.OnlineDoctorBookingHistoryApiResponse
import com.iprism.medrayder.models.onlinedoctorbookinghistory.OnlineDoctorBookingHistoryRequest
import com.iprism.medrayder.models.onlinedoctorrating.OnlineDoctorRatingApiResponse
import com.iprism.medrayder.models.onlinedoctorrating.OnlineDoctorRatingRequest
import com.iprism.medrayder.models.onlinedoctors.OnlineDoctorApiResponse
import com.iprism.medrayder.models.onlinedoctors.OnlineDoctorRequest
import com.iprism.medrayder.models.onlinedoctorscoupons.CouponsApiResponse
import com.iprism.medrayder.models.onlinedoctorscoupons.CouponRequest
import com.iprism.medrayder.models.onlinedoctorsinglebookingdetails.OnlineDoctorSingleBookingDetailsApiResponse
import com.iprism.medrayder.models.onlinedoctorsinglebookingdetails.OnlineDoctorSingleBookingDetailsRequest
import com.iprism.medrayder.models.onlinedoctorspeacilities.OnlineDoctorSpecialitiesApiResponse
import com.iprism.medrayder.models.onlinedoctorspeacilities.OnlineDoctorSpecialitiesRequest
import com.iprism.medrayder.network.MedRayderApi

class OnlineDoctorRepository {

    private val apiService = MedRayderApi.medRayderService

    suspend fun getOnlineDoctorSpecialities(request: OnlineDoctorSpecialitiesRequest): OnlineDoctorSpecialitiesApiResponse {
        return apiService.getOnlineDoctorsSpecialities(request)
    }

    suspend fun getOnlineDoctors(request: OnlineDoctorRequest): OnlineDoctorApiResponse {
        return apiService.getOnlineDoctors(request)
    }

    suspend fun getOnlineDoctorBookingDetails(request: OnlineDoctorBookingDetailsRequest): OnlineDoctorBookingDetailsApiResponse {
        return apiService.getOnlineDoctorBookingDetails(request)
    }

    suspend fun bookOnlineDoctor(request: OnlineDoctorBookingRequest): OnlineDoctorBookingApiResponse {
        return apiService.bookOnlineDoctor(request)
    }

    suspend fun getOnlineDoctorBookings(request: OnlineDoctorBookingHistoryRequest): OnlineDoctorBookingHistoryApiResponse {
        return apiService.getOnlineDoctorBookings(request)
    }

    suspend fun getOnlineDoctorCompletedBookings(request: OnlineDoctorBookingHistoryRequest): OnlineDoctorBookingHistoryApiResponse {
        return apiService.getOnlineDoctorCompletedBookings(request)
    }

    suspend fun insertOnlineDoctorRating(request: OnlineDoctorRatingRequest): OnlineDoctorRatingApiResponse {
        return apiService.insertOnlineDoctorRating(request)
    }

    suspend fun getOnlineDoctorBookingDetails(request: OnlineDoctorSingleBookingDetailsRequest): OnlineDoctorSingleBookingDetailsApiResponse {
        return apiService.getOnlineDoctorBookingDetails(request)
    }
}