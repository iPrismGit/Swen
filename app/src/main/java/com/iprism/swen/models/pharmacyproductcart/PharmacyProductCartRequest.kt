package com.iprism.swen.models.pharmacyproductcart

import com.google.gson.annotations.SerializedName

data class PharmacyProductCartRequest(

	@field:SerializedName("coupon_percentage")
	val couponPercentage: Int,

	@field:SerializedName("coupon_id")
	val couponId: Int,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("language")
	val language: String,

	@field:SerializedName("auth_token")
	val authToken: String,

	@field:SerializedName("order_type")
	val orderTYpe: String
)