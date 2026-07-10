package com.iprism.swen.repository

import com.iprism.swen.models.hospitals.HospitalsApiResponse
import com.iprism.swen.models.surgerysymptoms.SurgerySymptomsDoctorRequest
import com.iprism.swen.network.SwenAPi

class SurgerySymptomsRepository {

    private val apiService = SwenAPi.swenApiService

    suspend fun fetchSurgerySymptomHospitals(request : SurgerySymptomsDoctorRequest): HospitalsApiResponse {
        return apiService.fetchSurgerySymptomHospitals(request)
    }
}
