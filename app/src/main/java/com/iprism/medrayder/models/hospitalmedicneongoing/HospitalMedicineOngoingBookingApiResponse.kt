package com.iprism.medrayder.models.hospitalmedicneongoing

import com.google.gson.annotations.SerializedName

data class HospitalMedicineOngoingBookingApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)