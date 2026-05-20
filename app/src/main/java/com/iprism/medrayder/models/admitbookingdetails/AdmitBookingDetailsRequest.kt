package com.iprism.medrayder.models.admitbookingdetails

import com.google.gson.annotations.SerializedName

data class AdmitBookingDetailsRequest(

	@field:SerializedName("booking_id")
	val bookingId: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("auth_token")
	val authToken: String,

	@field:SerializedName("language")
	val lang: String
)