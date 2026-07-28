package com.iprism.swen.models.symptomdoctorbooking

import com.google.gson.annotations.SerializedName

data class SymptomDoctorBookingRequest(

	@field:SerializedName("symptom_id")
	val symptomId: Int,

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("transaction_id")
	val transactionId: String,

	@field:SerializedName("consultation_fee")
	val consultationFee: String,

	@field:SerializedName("coupon_percentage")
	val couponPercentage: Int,

	@field:SerializedName("fee")
	val fee: Int,

	@field:SerializedName("mobile")
	val mobile: String,

	@field:SerializedName("main_data_id")
	val mainDataId: Int,

	@field:SerializedName("doctor_id")
	val doctorId: Int,

	@field:SerializedName("payment_type")
	val paymentType: String,

	@field:SerializedName("coupon_id")
	val couponId: Int,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("slot_id")
	val slotId: Int,

	@field:SerializedName("patient_name")
	val patientName: String,

	@field:SerializedName("consult_type")
	val consultType: String,

	@field:SerializedName("time")
	val time: String,

	@field:SerializedName("auth_token")
	val authToken: String,

	@field:SerializedName("coupon_discount")
	val couponDiscount: String,

	@field:SerializedName("family_member_id")
	val familyMemberId: Int
)
