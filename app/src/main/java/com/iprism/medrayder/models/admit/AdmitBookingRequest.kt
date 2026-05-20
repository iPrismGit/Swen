package com.iprism.medrayder.models.admit

import com.google.gson.annotations.SerializedName

data class AdmitBookingRequest(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("slot_id")
	val slotId: Int,

	@field:SerializedName("cat_id")
	val catId: Int,

	@field:SerializedName("mobile")
	val mobile: String,

	@field:SerializedName("patient_name")
	val patientName: String,

	@field:SerializedName("view_type")
	val viewType: String,

	@field:SerializedName("language")
	val language: String,

	@field:SerializedName("time")
	val time: String,

	@field:SerializedName("auth_token")
	val authToken: String,

	@field:SerializedName("main_data_id")
	val mainDataId: Int,

	@field:SerializedName("family_member_id")
	val familyMemberId: Int
)