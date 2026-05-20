package com.iprism.swen.models.onlinedoctorscoupons

import com.google.gson.annotations.SerializedName

data class CouponRequest(

	@field:SerializedName("user_id")
	val userId: String,

	@field:SerializedName("language")
	val language: String,

	@field:SerializedName("auth_token")
	val authToken: String
)