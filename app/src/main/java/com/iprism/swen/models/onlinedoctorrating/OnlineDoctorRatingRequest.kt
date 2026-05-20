package com.iprism.swen.models.onlinedoctorrating

import com.google.gson.annotations.SerializedName

data class OnlineDoctorRatingRequest(

	@field:SerializedName("booking_id")
	val bookingId: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("rating")
	val rating: Int,

	@field:SerializedName("auth_token")
	val authToken: String
)
