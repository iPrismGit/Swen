package com.iprism.swen.models.diagnosticbooking

import com.google.gson.annotations.SerializedName

data class DiagnosticBookingApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)