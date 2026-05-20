package com.iprism.medrayder.models.pharmacyDetails

import com.google.gson.annotations.SerializedName

data class PharmacyDetailsApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)