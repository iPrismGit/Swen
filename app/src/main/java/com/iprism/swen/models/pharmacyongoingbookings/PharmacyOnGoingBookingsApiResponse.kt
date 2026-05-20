package com.iprism.swen.models.pharmacyongoingbookings

import com.google.gson.annotations.SerializedName

data class PharmacyOnGoingBookingsApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)