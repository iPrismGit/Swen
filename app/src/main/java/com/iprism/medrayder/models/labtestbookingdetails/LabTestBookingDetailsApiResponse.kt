package com.iprism.medrayder.models.labtestbookingdetails

import com.google.gson.annotations.SerializedName

data class LabTestBookingDetailsApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)