package com.iprism.swen.repository

import com.iprism.swen.models.diagnostictestbookings.DiagnosticTestsBookingsApiResponse
import com.iprism.swen.models.diagnostictests.DiagnosticTestsApiResponse
import com.iprism.swen.models.diagnostictimings.DiagnosticSlotsApiResponse
import com.iprism.swen.models.hospitaldiagnosticbooking.HospitalDiagnosticBookingApiResponse
import com.iprism.swen.models.hospitaldiagnosticbooking.HospitalDiagnosticBookingRequest
import com.iprism.swen.models.hospitaldiagnosticbookingdetails.HospitalDiagnosticTestBookingDetailsApiResponse
import com.iprism.swen.models.hospitaldiagnosticbookingdetails.HospitalDiagnosticTestBookingDetailsRequest
import com.iprism.swen.models.hospitaldiagnosticprescriptionbooking.HospitalDiagnosticPrescriptionBookingApiResponse
import com.iprism.swen.models.hospitaldiagnosticprescriptionbooking.HospitalDiagnosticPrescriptionBookingRequest
import com.iprism.swen.models.hospitaldiagnostictests.HospitalDiagnosticTestsRequest
import com.iprism.swen.models.hospitalmedicinetimeslots.HospitalDiagnosticTimeRequest
import com.iprism.swen.models.labtestbookings.LabTestBookingsRequest
import com.iprism.swen.network.SwenAPi

class HospitalDiagnosticsRepository {

    private val apiService = SwenAPi.swenApiService

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