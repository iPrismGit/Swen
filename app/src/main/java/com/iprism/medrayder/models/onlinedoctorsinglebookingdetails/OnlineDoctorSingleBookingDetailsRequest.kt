package com.iprism.medrayder.models.onlinedoctorsinglebookingdetails

import com.google.gson.annotations.SerializedName

data class OnlineDoctorSingleBookingDetailsRequest(

	@field:SerializedName("booking_id")
	val bookingId: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("auth_token")
	val authToken: String,

	@field:SerializedName("language")
	val lang: String
)