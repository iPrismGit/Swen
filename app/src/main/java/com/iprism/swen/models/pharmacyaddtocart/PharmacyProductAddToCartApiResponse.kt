package com.iprism.swen.models.pharmacyaddtocart

import com.google.gson.annotations.SerializedName

data class PharmacyProductAddToCartApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)