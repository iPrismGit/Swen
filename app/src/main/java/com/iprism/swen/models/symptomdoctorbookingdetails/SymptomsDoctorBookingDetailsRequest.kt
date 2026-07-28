package com.iprism.swen.models.symptomdoctorbookingdetails

import com.google.gson.annotations.SerializedName

data class SymptomsDoctorBookingDetailsRequest(

	@field:SerializedName("symptom_id")
	val symptomId: Int,

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("doctor_id")
	val doctorId: Int,

	@field:SerializedName("coupon_percentage")
	val couponPercentage: Int,

	@field:SerializedName("coupon_id")
	val couponId: Int,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("fee")
	val fee: Int,

	@field:SerializedName("view_type")
	val viewType: String,

	@field:SerializedName("auth_token")
	val authToken: String,

	@field:SerializedName("main_data_id")
	val mainDataId: Int,

	@field:SerializedName("family_member_id")
	val familyMemberId: Int
)
