package com.iprism.medrayder.models.hospitaldoctortimeslots

import com.google.gson.annotations.SerializedName

data class HospitalDoctorTimeSlotsRequest(

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

	@field:SerializedName("speciality_id")
	val specialityId: Int,

	@field:SerializedName("fee")
	val fee: Int,

	@field:SerializedName("view_type")
	val viewType: String,

	@field:SerializedName("language")
	val language: String,

	@field:SerializedName("auth_token")
	val authToken: String,

	@field:SerializedName("main_data_id")
	val mainDataId: Int,

	@field:SerializedName("consult_type")
	val consultType: String
)