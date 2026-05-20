package com.iprism.swen.models.hospitalmedbookingdetails

import com.google.gson.annotations.SerializedName

data class HospitalMedicineBookingDetailsApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)