package com.iprism.medrayder.models.hospitalpharmacyprescriptionbooking

import com.google.gson.annotations.SerializedName

data class HospitalPharmacyPrescriptionBookingApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)