package com.iprism.swen.models.pharmacyproductcart

import com.google.gson.annotations.SerializedName

data class BillSummary(

	@field:SerializedName("consultation_fee")
	val consultationFee: String,

	@field:SerializedName("coupon_percentage")
	val couponPercentage: String,

	@field:SerializedName("coupon_id")
	val couponId: Int,

	@field:SerializedName("fee")
	val fee: String,

	@field:SerializedName("coupon_discount")
	val couponDiscount: String
)