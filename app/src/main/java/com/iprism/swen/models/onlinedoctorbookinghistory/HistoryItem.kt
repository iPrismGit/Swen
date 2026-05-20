package com.iprism.swen.models.onlinedoctorbookinghistory

import com.google.gson.annotations.SerializedName

data class HistoryItem(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("coupon_percentage")
	val couponPercentage: Int,

	@field:SerializedName("gender")
	val gender: String,

	@field:SerializedName("speciality_id")
	val specialityId: Int,

	@field:SerializedName("fee")
	val fee: Int,

	@field:SerializedName("booking_id")
	val bookingId: String,

	@field:SerializedName("doctor_id")
	val doctorId: Int,

	@field:SerializedName("coupon_id")
	val couponId: Int,

	@field:SerializedName("patient_name")
	val patientName: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("exp")
	val exp: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("consultation_fee")
	val consultationFee: Int,

	@field:SerializedName("speciality_name")
	val specialityName: String,

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("unique_id")
	val uniqueId: String,

	@field:SerializedName("mobile")
	val mobile: Long,

	@field:SerializedName("consultations")
	val consultations: Int,

	@field:SerializedName("qualification")
	val qualification: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("slot_id")
	val slotId: Int,

	@field:SerializedName("dob")
	val dob: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("specialization")
	val specialization: String,

	@field:SerializedName("consult_type")
	val consultType: String,

	@field:SerializedName("time")
	val time: String,

	@field:SerializedName("coupon_discount")
	val couponDiscount: Int,

	@field:SerializedName("family_member_id")
	val familyMemberId: Int
)