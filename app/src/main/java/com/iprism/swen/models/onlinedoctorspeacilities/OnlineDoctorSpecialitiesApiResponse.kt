package com.iprism.swen.models.onlinedoctorspeacilities

import com.google.gson.annotations.SerializedName

data class OnlineDoctorSpecialitiesApiResponse(

    @field:SerializedName("response")
	val onlineDoctorSpecialitiesResponse: OnlineDoctorSpecialitiesResponse,

    @field:SerializedName("message")
	val message: String,

    @field:SerializedName("status")
	val status: Boolean
)