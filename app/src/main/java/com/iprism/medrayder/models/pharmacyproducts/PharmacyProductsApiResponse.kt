package com.iprism.medrayder.models.pharmacyproducts

import com.google.gson.annotations.SerializedName

data class PharmacyProductsApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)