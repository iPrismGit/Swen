package com.iprism.medrayder.models.labtestbookings

import com.google.gson.annotations.SerializedName

data class LabTestBookingsApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)