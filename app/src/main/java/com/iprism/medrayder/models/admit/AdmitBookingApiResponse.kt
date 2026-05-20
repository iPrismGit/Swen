package com.iprism.medrayder.models.admit

import com.google.gson.annotations.SerializedName

data class AdmitBookingApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)