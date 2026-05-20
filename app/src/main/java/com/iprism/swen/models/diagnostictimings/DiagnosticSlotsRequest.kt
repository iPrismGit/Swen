package com.iprism.swen.models.diagnostictimings

import com.google.gson.annotations.SerializedName

data class DiagnosticSlotsRequest(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("fee")
	val fee: String,

	@field:SerializedName("count")
	val count: Int,

	@field:SerializedName("view_type")
	val viewType: String,

	@field:SerializedName("language")
	val language: String,

	@field:SerializedName("auth_token")
	val authToken: String,

	@field:SerializedName("diagnostic_id")
	val diagnosticId: Int,

	@field:SerializedName("test_id")
	val testId: Int,

	@field:SerializedName("family_member_id")
	val familyMemberId: String,

	@field:SerializedName("coupon_id")
	val couponId: String,

	@field:SerializedName("coupon_percentage")
	val couponPercentage: String,
)