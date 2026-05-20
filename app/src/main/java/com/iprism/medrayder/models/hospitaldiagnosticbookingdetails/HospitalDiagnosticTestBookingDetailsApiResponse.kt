package com.iprism.medrayder.models.hospitaldiagnosticbookingdetails

import com.google.gson.annotations.SerializedName

data class HospitalDiagnosticTestBookingDetailsApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)