package com.iprism.ecmuser.models.doctorrating

import com.google.gson.annotations.SerializedName

data class DoctorRatingApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class Response(
	val any: Any? = null
)
