package com.iprism.swen.models.onlinedoctorbooking

import com.google.gson.annotations.SerializedName

data class OnlineDoctorBookingApiResponse(

    @field:SerializedName("response")
	val onlineDoctorBookingResponse: OnlineDoctorBookingResponse,

    @field:SerializedName("message")
	val message: String,

    @field:SerializedName("status")
	val status: Boolean
)