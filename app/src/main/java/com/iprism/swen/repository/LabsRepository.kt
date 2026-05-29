package com.iprism.swen.repository

import com.iprism.swen.models.labbooking.LabBookingApiResponse
import com.iprism.swen.models.labbooking.LabBookingRequest
import com.iprism.swen.models.labcenters.LabCentersApiResponse
import com.iprism.swen.models.labcenters.LabCentersRequest
import com.iprism.swen.models.labtestbookingdetails.LabTestBookingDetailsApiResponse
import com.iprism.swen.models.labtestbookingdetails.LabTestBookingDetailsRequest
import com.iprism.swen.models.labtestbookings.LabTestBookingsApiResponse
import com.iprism.swen.models.labtestbookings.LabTestBookingsRequest
import com.iprism.swen.models.labtestprescriptionbooking.LabTestPrescriptionBookingApiResponse
import com.iprism.swen.models.labtestprescriptionbooking.LabTestPrescriptionBookingRequest
import com.iprism.swen.models.labtests.LabTestsApiResponse
import com.iprism.swen.models.labtests.LabTestsRequest
import com.iprism.swen.models.labtestslots.LabTestSlotsApiResponse
import com.iprism.swen.models.labtestslots.LabTestSlotsRequest
import com.iprism.swen.network.SwenAPi

class LabsRepository {

    private val apiService = SwenAPi.swenApiService

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