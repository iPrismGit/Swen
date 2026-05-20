package com.iprism.medrayder.models.hospitalmedicineaddtocart

import com.google.gson.annotations.SerializedName

data class HospitalMedineProductsAddToCartApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)