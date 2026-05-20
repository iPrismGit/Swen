package com.iprism.swen.models.diagnosticbookingdetails

import com.google.gson.annotations.SerializedName

data class DiagnosticTestBookingDetailsApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)