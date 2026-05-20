package com.iprism.swen.models.admitbookingdetails

import com.google.gson.annotations.SerializedName

data class AdmitBookingDetailsAPiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)