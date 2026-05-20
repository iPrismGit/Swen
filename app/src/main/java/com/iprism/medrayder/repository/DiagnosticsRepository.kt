package com.iprism.medrayder.repository

import com.iprism.medrayder.models.diagnosticbooking.DiagnosticBookingApiResponse
import com.iprism.medrayder.models.diagnosticbooking.DiagnosticBookingRequest
import com.iprism.medrayder.models.diagnosticbookingdetails.DiagnosticTestBookingDetailsApiResponse
import com.iprism.medrayder.models.diagnosticbookingdetails.DiagnosticTestBookingDetailsRequest
import com.iprism.medrayder.models.diagnosticprescriptionbooking.DiagnosticPrescriptionBookingRequest
import com.iprism.medrayder.models.diagnostictestbookings.DiagnosticTestsBookingsApiResponse
import com.iprism.medrayder.models.diagnostictests.DiagnosticTestsApiResponse
import com.iprism.medrayder.models.diagnostictests.DiagnosticTestsRequest
import com.iprism.medrayder.models.diagnostictimings.DiagnosticSlotsApiResponse
import com.iprism.medrayder.models.diagnostictimings.DiagnosticSlotsRequest
import com.iprism.medrayder.models.dignosticcenters.DiagnosticCentersApiResponse
import com.iprism.medrayder.models.dignosticcenters.DiagnosticCentersRequest
import com.iprism.medrayder.models.labtestbookings.LabTestBookingsApiResponse
import com.iprism.medrayder.models.labtestbookings.LabTestBookingsRequest
import com.iprism.medrayder.models.labtestprescriptionbooking.LabTestPrescriptionBookingApiResponse
import com.iprism.medrayder.models.labtestprescriptionbooking.LabTestPrescriptionBookingRequest
import com.iprism.medrayder.models.login.LoginApiResponse
import com.iprism.medrayder.network.MedRayderApi

class DiagnosticsRepository {

    private val apiService = MedRayderApi.medRayderService

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