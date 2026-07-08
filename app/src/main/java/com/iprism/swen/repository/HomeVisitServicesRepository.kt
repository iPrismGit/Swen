package com.iprism.swen.repository

import com.iprism.swen.models.homeservices.HomeServicesApiResponse
import com.iprism.swen.models.homeservices.HomeServicesRequest
import com.iprism.swen.models.homeservicesbooking.HomeServicesBookingApiResponse
import com.iprism.swen.models.homeservicesbooking.HomeServicesBookingRequest
import com.iprism.swen.network.SwenAPi

class HomeVisitServicesRepository {

    private val apiService = SwenAPi.swenApiService

    suspend fun fetchHomeVisitServices(req : HomeServicesRequest): HomeServicesApiResponse {
        return apiService.fetchHomeVisitServices(req)
    }

    suspend fun bookHomeVisitService(req: HomeServicesBookingRequest): HomeServicesBookingApiResponse {
        return apiService.bookHomeVisitService(req)
    }
}