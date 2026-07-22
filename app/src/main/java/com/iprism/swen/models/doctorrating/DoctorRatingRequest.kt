package com.iprism.ecmuser.models.doctorrating

import com.google.gson.annotations.SerializedName

data class DoctorRatingRequest(

	@field:SerializedName("booking_id")
	val bookingId: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("rating")
	val rating: Int,

	@field:SerializedName("booking_type")
	val bookingType: String,

	@field:SerializedName("auth_token")
	val authToken: String
)
