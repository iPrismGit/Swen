package com.iprism.swen.models

data class TestimonialVideosApiRequest(

    val auth_token: String,
    val main_data_id: String,
    val page: Int,
    val user_id: String,
    val view_type: String

)