package com.iprism.swen.models.surgeondoctorsinglebookingdetails

import com.google.gson.annotations.SerializedName

data class SurgeonDoctorSingleBookingDetailsRequest(

	@field:SerializedName("booking_id")
	val bookingId: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("auth_token")
	val authToken: String
)
