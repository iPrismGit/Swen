package com.iprism.medrayder.models.hospitaldiagnosticbookingdetails

import com.google.gson.annotations.SerializedName

data class HospitalDiagnosticTestBookingDetailsRequest(

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