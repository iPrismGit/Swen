package com.iprism.swen.models.labtestbookingdetails

import com.google.gson.annotations.SerializedName

data class LabTestBookingDetailsRequest(

	@field:SerializedName("booking_id")
	val bookingId: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("booking_type")
	val bookingType: String,

	@field:SerializedName("language")
	val language: String,

	@field:SerializedName("auth_token")
	val authToken: String
)