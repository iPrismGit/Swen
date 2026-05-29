package com.iprism.swen.repository

import com.iprism.swen.models.diagnosticbooking.DiagnosticBookingApiResponse
import com.iprism.swen.models.diagnosticbooking.DiagnosticBookingRequest
import com.iprism.swen.models.diagnosticbookingdetails.DiagnosticTestBookingDetailsApiResponse
import com.iprism.swen.models.diagnosticbookingdetails.DiagnosticTestBookingDetailsRequest
import com.iprism.swen.models.diagnosticprescriptionbooking.DiagnosticPrescriptionBookingRequest
import com.iprism.swen.models.diagnostictestbookings.DiagnosticTestsBookingsApiResponse
import com.iprism.swen.models.diagnostictests.DiagnosticTestsApiResponse
import com.iprism.swen.models.diagnostictests.DiagnosticTestsRequest
import com.iprism.swen.models.diagnostictimings.DiagnosticSlotsApiResponse
import com.iprism.swen.models.diagnostictimings.DiagnosticSlotsRequest
import com.iprism.swen.models.dignosticcenters.DiagnosticCentersApiResponse
import com.iprism.swen.models.dignosticcenters.DiagnosticCentersRequest
import com.iprism.swen.models.labtestbookings.LabTestBookingsRequest
import com.iprism.swen.models.labtestprescriptionbooking.LabTestPrescriptionBookingApiResponse
import com.iprism.swen.network.SwenAPi

class DiagnosticsRepository {

    private val apiService = SwenAPi.swenApiService

    suspend fun getDiagnostics(request: DiagnosticCentersRequest): DiagnosticCentersApiResponse {
        return apiService.getDiagnosticCenters(request)
    }

    suspend fun fetchDiagnosticTests(request: DiagnosticTestsRequest): DiagnosticTestsApiResponse {
        return apiService.fetchDiagnosticTests(request)
    }

    suspend fun fetchDiagnosticBookingDetails(request: DiagnosticSlotsRequest): DiagnosticSlotsApiResponse {
        return apiService.fetchDiagnosticBookingDetails(request)
    }

    suspend fun bookDiagnostic(request: DiagnosticBookingRequest): DiagnosticBookingApiResponse {
        return apiService.bookDiagnostic(request)
    }

    suspend fun bookPrescriptionDiagnosticTest(request: DiagnosticPrescriptionBookingRequest): LabTestPrescriptionBookingApiResponse {
        return apiService.bookPrescriptionDiagnosticTest(request)
    }

    suspend fun fetchDiagnosticTestsOngoingBookings(request: LabTestBookingsRequest) : DiagnosticTestsBookingsApiResponse {
        return apiService.fetchDiagnosticTestsOngoingBookings(request)
    }

    suspend fun fetchDiagnosticTestsCompletedBookings(request: LabTestBookingsRequest) : DiagnosticTestsBookingsApiResponse {
        return apiService.fetchDiagnosticTestsCompletedBookings(request)
    }

    suspend fun fetchDiagnosticTestBookingDetails(request: DiagnosticTestBookingDetailsRequest) : DiagnosticTestBookingDetailsApiResponse {
        return apiService.fetchDiagnosticTestBookingDetails(request)
    }
}