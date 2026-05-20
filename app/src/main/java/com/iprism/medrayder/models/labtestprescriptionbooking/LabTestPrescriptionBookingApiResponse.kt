package com.iprism.medrayder.models.labtestprescriptionbooking

import com.google.gson.annotations.SerializedName

data class LabTestPrescriptionBookingApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)