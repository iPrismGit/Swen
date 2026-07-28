package com.iprism.swen.repository

import com.iprism.ecmuser.models.doctorrating.DoctorRatingApiResponse
import com.iprism.ecmuser.models.doctorrating.DoctorRatingRequest
import com.iprism.swen.models.symptomdoctorbooking.SymptomDoctorBookingApiResponse
import com.iprism.swen.models.symptomdoctorbooking.SymptomDoctorBookingRequest
import com.iprism.swen.models.symptomdoctorbookingdetails.SymptomDoctorBookingDetailsApiResponse
import com.iprism.swen.models.symptomdoctorbookingdetails.SymptomsDoctorBookingDetailsRequest
import com.iprism.swen.models.symptomdoctorbookings.SymptomDoctorBookingsApiResponse
import com.iprism.swen.models.symptomdoctorbookings.SymptomDoctorBookingsRequest
import com.iprism.swen.models.symptomdoctorsinglebookingdetails.SymptomDoctorSingleBookingDetailsApiResponse
import com.iprism.swen.models.symptomdoctorsinglebookingdetails.SymptomDoctorSingleBookingDetailsRequest
import com.iprism.swen.models.symptomsdoctors.SymptomsDoctorsApiResponse
import com.iprism.swen.models.symptomsdoctors.SymptomsDoctorsRequest
import com.iprism.swen.network.SwenAPi

class SymptomsDoctorRepository {

    private val apiService = SwenAPi.swenApiService

    /*suspend fun fetchAllSymptoms(request: AllSymptomsRequest): AllSymptomsApiResponse {
        return apiService.fetchAllSymptoms(request)
    }*/

    suspend fun fetchAllSymptomDoctors(request: SymptomsDoctorsRequest): SymptomsDoctorsApiResponse {
        return apiService.fetchSymptomDoctors(request)
    }

    suspend fun fetchSymptomDoctorBookingDetails(request: SymptomsDoctorBookingDetailsRequest): SymptomDoctorBookingDetailsApiResponse {
        return apiService.fetchSymptomDoctorBookingDetails(request)
    }

    suspend fun bookSymptomDoctor(request: SymptomDoctorBookingRequest): SymptomDoctorBookingApiResponse {
        return apiService.bookSymptomDoctor(request)
    }

    suspend fun fetchSymptomDoctorOngoingBookings(request: SymptomDoctorBookingsRequest): SymptomDoctorBookingsApiResponse {
        return apiService.fetchSymptomDoctorOngoingBookings(request)
    }

    suspend fun fetchSymptomDoctorCompletedBookings(request: SymptomDoctorBookingsRequest): SymptomDoctorBookingsApiResponse {
        return apiService.fetchSymptomDoctorCompletedBookings(request)
    }

    suspend fun fetchSymptomDoctorSingleBookingDetails(request: SymptomDoctorSingleBookingDetailsRequest): SymptomDoctorSingleBookingDetailsApiResponse {
        return apiService.fetchSymptomDoctorSingleBookingDetails(request)
    }

    suspend fun insertDoctorRating(request: DoctorRatingRequest): DoctorRatingApiResponse {
        return apiService.insertDoctorRating(request)
    }
}