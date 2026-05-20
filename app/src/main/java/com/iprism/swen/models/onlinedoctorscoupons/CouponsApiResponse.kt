package com.iprism.swen.models.onlinedoctorscoupons

import com.google.gson.annotations.SerializedName

data class CouponsApiResponse(

	@field:SerializedName("response")
	val couponsResponse: CouponsResponse,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)