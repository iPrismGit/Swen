package com.iprism.medrayder.models.hospitalambulancebookings

import com.google.gson.annotations.SerializedName

data class HospitalAmbulanceBookingApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)