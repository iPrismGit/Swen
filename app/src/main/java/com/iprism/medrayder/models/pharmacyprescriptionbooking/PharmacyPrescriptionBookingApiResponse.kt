package com.iprism.medrayder.models.pharmacyprescriptionbooking

import com.google.gson.annotations.SerializedName

data class PharmacyPrescriptionBookingApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)