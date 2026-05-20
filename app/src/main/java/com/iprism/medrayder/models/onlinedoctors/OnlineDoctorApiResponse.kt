package com.iprism.medrayder.models.onlinedoctors

import com.google.gson.annotations.SerializedName

data class OnlineDoctorApiResponse(

	@field:SerializedName("response")
	val response: OnlineDoctorResponse,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)