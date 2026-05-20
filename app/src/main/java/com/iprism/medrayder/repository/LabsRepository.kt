package com.iprism.medrayder.repository

import com.iprism.medrayder.models.diagnosticbooking.DiagnosticBookingApiResponse
import com.iprism.medrayder.models.diagnosticbooking.DiagnosticBookingRequest
import com.iprism.medrayder.models.diagnostictests.DiagnosticTestsApiResponse
import com.iprism.medrayder.models.diagnostictests.DiagnosticTestsRequest
import com.iprism.medrayder.models.diagnostictimings.DiagnosticSlotsApiResponse
import com.iprism.medrayder.models.diagnostictimings.DiagnosticSlotsRequest
import com.iprism.medrayder.models.dignosticcenters.DiagnosticCentersApiResponse
import com.iprism.medrayder.models.dignosticcenters.DiagnosticCentersRequest
import com.iprism.medrayder.models.labbooking.LabBookingApiResponse
import com.iprism.medrayder.models.labbooking.LabBookingRequest
import com.iprism.medrayder.models.labcenters.LabCentersApiResponse
import com.iprism.medrayder.models.labcenters.LabCentersRequest
import com.iprism.medrayder.models.labtestbookingdetails.LabTestBookingDetailsApiResponse
import com.iprism.medrayder.models.labtestbookingdetails.LabTestBookingDetailsRequest
import com.iprism.medrayder.models.labtestbookings.LabTestBookingsApiResponse
import com.iprism.medrayder.models.labtestbookings.LabTestBookingsRequest
import com.iprism.medrayder.models.labtestprescriptionbooking.LabTestPrescriptionBookingApiResponse
import com.iprism.medrayder.models.labtestprescriptionbooking.LabTestPrescriptionBookingRequest
import com.iprism.medrayder.models.labtests.LabTestsApiResponse
import com.iprism.medrayder.models.labtests.LabTestsRequest
import com.iprism.medrayder.models.labtestslots.LabTestSlotsApiResponse
import com.iprism.medrayder.models.labtestslots.LabTestSlotsRequest
import com.iprism.medrayder.models.login.LoginApiResponse
import com.iprism.medrayder.network.MedRayderApi

class LabsRepository {

    private val apiService = MedRayderApi.medRayderService

    suspend fun fetchLabs(request: LabCentersRequest): LabCentersApiResponse {
        return apiService.fetchLabCenters(request)
    }

    suspend fun fetchLabs(request: LabTestsRequest): LabTestsApiResponse {
        return apiService.fetchLabTests(request)
    }

    suspend fun fetchLabTestBookingDetails(request: LabTestSlotsRequest): LabTestSlotsApiResponse {
        return apiService.fetchLabBookingDetails(request)
    }

    suspend fun bookLab(request: LabBookingRequest): LabBookingApiResponse {
        return apiService.bookLab(request)
    }

    suspend fun bookPrescriptionLabTest(request: LabTestPrescriptionBookingRequest): LabTestPrescriptionBookingApiResponse {
        return apiService.bookPrescriptionLabTest(request)
    }

    suspend fun fetchLabTestsOngoingBookings(request: LabTestBookingsRequest) : LabTestBookingsApiResponse {
        return apiService.fetchLabTestsOngoingBookings(request)
    }

    suspend fun fetchLabTestsCompletedBookings(request: LabTestBookingsRequest) : LabTestBookingsApiResponse {
        return apiService.fetchLabTestsCompletedBookings(request)
    }

    suspend fun fetchLabTestsBookingDetails(request: LabTestBookingDetailsRequest) : LabTestBookingDetailsApiResponse {
        return apiService.fetchLabTestBookingDetails(request)
    }
}