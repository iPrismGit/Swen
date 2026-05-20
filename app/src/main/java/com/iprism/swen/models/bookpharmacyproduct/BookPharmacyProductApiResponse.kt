package com.iprism.swen.models.bookpharmacyproduct

import com.google.gson.annotations.SerializedName

data class BookPharmacyProductApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)