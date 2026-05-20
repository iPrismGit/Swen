package com.iprism.swen.models.hospitalmedicineproducts

import com.google.gson.annotations.SerializedName

data class HospitalMedicineProductsApiResponse(


	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)