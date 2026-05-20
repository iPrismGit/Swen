package com.iprism.swen.models.hospitaldoctors

import com.google.gson.annotations.SerializedName

data class HospitalDoctorsApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)