package com.iprism.swen.repository

import com.iprism.ecmuser.models.healthmedia.HealthMediaApiResponse
import com.iprism.ecmuser.models.healthmedia.HealthMediaRequest
import com.iprism.swen.network.SwenAPi

class HealthMediaRepository {

    private val apiService = SwenAPi.swenApiService

    suspend fun fetchHealthMedia(request: HealthMediaRequest): HealthMediaApiResponse {
        return apiService.fetchHealthMedia(request)
    }
}