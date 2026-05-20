package com.iprism.medrayder.models.hospitaladmitongoingbookings

import com.google.gson.annotations.SerializedName

data class HospitalAdmissionOnGoingApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)