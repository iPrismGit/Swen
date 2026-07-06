package com.iprism.ecmuser.models

data class HospitalAssistanceApiRequest(

    val auth_token: String,
    val main_data_id: String,
    val user_id: String

)