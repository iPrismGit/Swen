package com.iprism.swen.models.symptomdoctorsinglebookingdetails

import com.google.gson.annotations.SerializedName

data class SymptomDoctorSingleBookingDetailsRequest(

	@field:SerializedName("booking_id")
	val bookingId: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("auth_token")
	val authToken: String
)
