package com.iprism.swen.models.ambulancebooking

import com.google.gson.annotations.SerializedName

data class AmbulanceBookingApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)