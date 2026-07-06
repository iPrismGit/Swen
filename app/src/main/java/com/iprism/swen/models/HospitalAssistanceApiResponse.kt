package com.iprism.ecmuser.models

data class HospitalAssistanceApiResponse(

    val message: String,
    val response: HospitalAssistanceResponse,
    val status: Boolean

)

data class HospitalAssistanceResponse(

    val id: Int,
    val mobile: String

)