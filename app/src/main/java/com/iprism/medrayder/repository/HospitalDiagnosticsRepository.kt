package com.iprism.medrayder.repository

import com.iprism.medrayder.models.diagnostictestbookings.DiagnosticTestsBookingsApiResponse
import com.iprism.medrayder.models.diagnostictests.DiagnosticTestsApiResponse
import com.iprism.medrayder.models.diagnostictimings.DiagnosticSlotsApiResponse
import com.iprism.medrayder.models.hospitaldiagnosticbooking.HospitalDiagnosticBookingApiResponse
import com.iprism.medrayder.models.hospitaldiagnosticbooking.HospitalDiagnosticBookingRequest
import com.iprism.medrayder.models.hospitaldiagnosticbookingdetails.HospitalDiagnosticTestBookingDetailsApiResponse
import com.iprism.medrayder.models.hospitaldiagnosticbookingdetails.HospitalDiagnosticTestBookingDetailsRequest
import com.iprism.medrayder.models.hospitaldiagnosticprescriptionbooking.HospitalDiagnosticPrescriptionBookingApiResponse
import com.iprism.medrayder.models.hospitaldiagnosticprescriptionbooking.HospitalDiagnosticPrescriptionBookingRequest
import com.iprism.medrayder.models.hospitaldiagnostictests.HospitalDiagnosticTestsRequest
import com.iprism.medrayder.models.hospitalmedicinetimeslots.HospitalDiagnosticTimeRequest
import com.iprism.medrayder.models.labtestbookings.LabTestBookingsRequest
import com.iprism.medrayder.network.MedRayderApi

class HospitalDiagnosticsRepository {

    private val apiService = MedRayderApi.medRayderService

    suspend fun fetchDiagnosticTests(request: HospitalDiagnosticTestsRequest): DiagnosticTestsApiResponse {
        return apiService.fetchHospitalDiagnosticTests(request)
    }

    suspend fun fetchHospitalDiagnosticTimeSlots(request: HospitalDiagnosticTimeRequest): DiagnosticSlotsApiResponse {
        return apiService.fetchHospitalDiagnosticTimeSlots(request)
    }

    suspend fun bookHospitalDiagnostic(request: HospitalDiagnosticBookingRequest): HospitalDiagnosticBookingApiResponse {
        return apiService.bookHospitalDiagnostic(request)
    }

    suspend fun fetchHospitalDiagnosticTestsOngoingBookings(request: LabTestBookingsRequest) : DiagnosticTestsBookingsApiResponse {
        return apiService.fetchHospitalDiagnosticTestsOngoingBookings(request)
    }

    suspend fun fetchHospitalDiagnosticTestsCompletedBookings(request: LabTestBookingsRequest) : DiagnosticTestsBookingsApiResponse {
        return apiService.fetchHospitalDiagnosticTestsCompletedBookings(request)
    }

    suspend fun fetchHospitalDiagnosticTestBookingDetails(request: HospitalDiagnosticTestBookingDetailsRequest) : HospitalDiagnosticTestBookingDetailsApiResponse {
        return apiService.fetchHospitalDiagnosticTestBookingDetails(request)
    }

    suspend fun bookHospitalDiagnosticPrescriptionBooking(request: HospitalDiagnosticPrescriptionBookingRequest) : HospitalDiagnosticPrescriptionBookingApiResponse {
        return apiService.bookHospitalDiagnosticPrescriptionBooking(request)
    }
}