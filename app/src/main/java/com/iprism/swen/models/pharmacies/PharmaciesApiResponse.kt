package com.iprism.swen.models.pharmacies

import com.google.gson.annotations.SerializedName

data class PharmaciesApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)