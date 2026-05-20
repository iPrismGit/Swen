package com.iprism.swen.models.onlinedoctorsinglebookingdetails

import com.google.gson.annotations.SerializedName

data class OnlineDoctorSingleBookingDetailsApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)