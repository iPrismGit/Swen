package com.iprism.swen.models

import com.iprism.ecmuser.models.healthmedia.Pagination

data class TestimonialVideosApiResponse(

    val message: String,
    val response: TestimonialVideosResponse,
    val status: Boolean

)

data class TestimonialVideosResponse(

    val pagination: Pagination,
    val health_talks: List<Video>

)

data class Video(

    val id: String,
    val image: String,
    val link: String,
    val main_data_id: String

)