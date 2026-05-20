package com.iprism.swen.repository

import com.iprism.swen.models.onlinedoctorbooking.OnlineDoctorBookingApiResponse
import com.iprism.swen.models.onlinedoctorbooking.OnlineDoctorBookingRequest
import com.iprism.swen.models.onlinedoctorbookingdetails.OnlineDoctorBookingDetailsApiResponse
import com.iprism.swen.models.onlinedoctorbookingdetails.OnlineDoctorBookingDetailsRequest
import com.iprism.swen.models.onlinedoctorbookinghistory.OnlineDoctorBookingHistoryApiResponse
import com.iprism.swen.models.onlinedoctorbookinghistory.OnlineDoctorBookingHistoryRequest
import com.iprism.swen.models.onlinedoctorrating.OnlineDoctorRatingApiResponse
import com.iprism.swen.models.onlinedoctorrating.OnlineDoctorRatingRequest
import com.iprism.swen.models.onlinedoctors.OnlineDoctorApiResponse
import com.iprism.swen.models.onlinedoctors.OnlineDoctorRequest
import com.iprism.swen.models.onlinedoctorsinglebookingdetails.OnlineDoctorSingleBookingDetailsApiResponse
import com.iprism.swen.models.onlinedoctorsinglebookingdetails.OnlineDoctorSingleBookingDetailsRequest
import com.iprism.swen.models.onlinedoctorspeacilities.OnlineDoctorSpecialitiesApiResponse
import com.iprism.swen.models.onlinedoctorspeacilities.OnlineDoctorSpecialitiesRequest
import com.iprism.swen.network.MedRayderApi

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