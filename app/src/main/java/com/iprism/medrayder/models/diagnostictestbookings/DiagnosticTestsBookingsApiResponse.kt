package com.iprism.medrayder.models.diagnostictestbookings

import com.google.gson.annotations.SerializedName

data class DiagnosticTestsBookingsApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)