package com.iprism.medrayder.models.hospitalmedicinebooking

import com.google.gson.annotations.SerializedName

data class HospitalMedicineBookingApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)