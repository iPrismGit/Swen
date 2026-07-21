package com.iprism.swen.repository

import com.iprism.swen.models.allsurgeonsymptoms.AllSurgeonSymptomsApiResponse
import com.iprism.swen.models.allsurgeonsymptoms.AllSurgeonSymptomsRequest
import com.iprism.swen.network.SwenAPi

class SurgeonSymptomsDoctorRepository {

    private val apiService = SwenAPi.swenApiService

    suspend fun fetchAllSurgeonSymptoms(request: AllSurgeonSymptomsRequest): AllSurgeonSymptomsApiResponse {
        return apiService.fetchAllSurgeonSymptoms(request)
    }

   /* suspend fun fetchSurgeonSymptomDoctors(request: SurgeonSymptomDoctorsRequest): SurgenSymptomDoctorsApiResponse {
        return apiService.fetchSurgeonSymptomDoctors(request)
    }*/

   /* suspend fun fetchSurgeonDoctorBookingDetails(request: SurgeonDoctorBookingDetailsRequest): SurgeonDoctorBookingDetailsApiResponse {
        return apiService.fetchSurgeonDoctorBookingDetails(request)
    }

    suspend fun bookSurgeonDoctor(request: SurgeonDoctorBookingRequest): SurgeonDoctorBookingApiResponse {
        return apiService.bookSurgeonDoctor(request)
    }

    suspend fun fetchSurgeonDoctorOngoingBookings(request: SurgeonDoctorBookingsRequest): SurgeonDoctorBookingsApiResponse {
        return apiService.fetchSurgeonDoctorOngoingBookings(request)
    }

    suspend fun fetchSurgeonDoctorCompletedBookings(request: SurgeonDoctorBookingsRequest): SurgeonDoctorBookingsApiResponse {
        return apiService.fetchSurgeonDoctorCompletedBookings(request)
    }

    suspend fun fetchSurgeonDoctorSingleBookingDetails(request: SurgeonDoctorSingleBookingDetailsRequest): SurgeonDoctorSingleBookingDetailsApiResponse {
        return apiService.fetchSurgeonDoctorSingleBookingDetails(request)
    }

    suspend fun insertDoctorRating(request: DoctorRatingRequest): DoctorRatingApiResponse {
        return apiService.insertDoctorRating(request)
    }*/
}