package com.iprism.swen.models.hospitalmedbookingdetails

import com.google.gson.annotations.SerializedName

data class HospitalMedicineBookingDetailsRequest(

	@field:SerializedName("booking_id")
	val bookingId: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("language")
	val language: String,

	@field:SerializedName("booking_type")
	val bookingType: String,

	@field:SerializedName("auth_token")
	val authToken: String
)