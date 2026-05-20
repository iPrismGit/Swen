package com.iprism.swen.models.hospitaldiagnosticprescriptionbooking

import com.google.gson.annotations.SerializedName

data class HospitalDiagnosticPrescriptionBookingApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)