package com.iprism.swen.models.hospitals

import com.google.gson.annotations.SerializedName

data class HospitalsApiResponse(

	@field:SerializedName("response")
	val hospitalResponse: HospitalResponse,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)