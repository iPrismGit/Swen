package com.iprism.swen.repository

import com.iprism.swen.models.hospitals.HospitalsApiResponse
import com.iprism.swen.models.surgerysymptoms.SurgerySymptomsDoctorRequest
import com.iprism.swen.network.SwenAPi

class SymptomsRepository {

    private val apiService = SwenAPi.swenApiService

    suspend fun fetchSymptomHospitals(request : SurgerySymptomsDoctorRequest): HospitalsApiResponse {
        return apiService.fetchSymptomHospitals(request)
    }
}
