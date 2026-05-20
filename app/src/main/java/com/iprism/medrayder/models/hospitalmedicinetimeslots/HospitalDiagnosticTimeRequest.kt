package com.iprism.medrayder.models.hospitalmedicinetimeslots

import com.google.gson.annotations.SerializedName

data class HospitalDiagnosticTimeRequest(

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

	@field:SerializedName("main_data_id")
	val mainDataId: Int,

	@field:SerializedName("test_id")
	val testId: Int,

	@field:SerializedName("family_member_id")
	val familyMemberId: String,

	@field:SerializedName("coupon_id")
	val couponId: String,

	@field:SerializedName("coupon_percentage")
	val couponPercentage: String,
)