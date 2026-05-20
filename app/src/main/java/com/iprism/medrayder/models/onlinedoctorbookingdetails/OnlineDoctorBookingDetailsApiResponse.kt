package com.iprism.medrayder.models.onlinedoctorbookingdetails

import com.google.gson.annotations.SerializedName

data class OnlineDoctorBookingDetailsApiResponse(

    @field:SerializedName("response")
	val response: OnlineDoctorBookingResponse,

    @field:SerializedName("message")
	val message: String,

    @field:SerializedName("status")
	val status: Boolean
)