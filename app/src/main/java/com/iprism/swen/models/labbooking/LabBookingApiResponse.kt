package com.iprism.swen.models.labbooking

import com.google.gson.annotations.SerializedName

data class LabBookingApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)