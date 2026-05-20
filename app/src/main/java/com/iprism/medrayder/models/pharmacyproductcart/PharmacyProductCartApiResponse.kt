package com.iprism.medrayder.models.pharmacyproductcart

import com.google.gson.annotations.SerializedName

data class PharmacyProductCartApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)