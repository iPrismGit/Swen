package com.iprism.swen.models.ambulancetracking

import com.google.gson.annotations.SerializedName

data class AmbulanceTrackingApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class Response(

	@field:SerializedName("booking_id")
	val bookingId: String,

	@field:SerializedName("created_on")
	val createdOn: String,

	@field:SerializedName("lon")
	val lon: Double,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("lat")
	val lat: Double
)
