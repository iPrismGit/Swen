package com.iprism.medrayder.models.hospitaldoctorbooking

import com.google.gson.annotations.SerializedName

data class HospitalDoctorBookingApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)