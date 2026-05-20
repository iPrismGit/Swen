package com.iprism.swen.models.hospitaldoctorbooking

import com.google.gson.annotations.SerializedName

data class HospitalDoctorBookingRequest(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("transaction_id")
	val transactionId: String,

	@field:SerializedName("consultation_fee")
	val consultationFee: String,

	@field:SerializedName("coupon_percentage")
	val couponPercentage: String,

	@field:SerializedName("speciality_id")
	val specialityId: String,

	@field:SerializedName("fee")
	val fee: String,

	@field:SerializedName("mobile")
	val mobile: String,

	@field:SerializedName("main_data_id")
	val mainDataId: String,

	@field:SerializedName("coupon_name")
	val couponName: String,

	@field:SerializedName("doctor_id")
	val doctorId: String,

	@field:SerializedName("payment_type")
	val paymentType: String,

	@field:SerializedName("coupon_id")
	val couponId: String,

	@field:SerializedName("user_id")
	val userId: String,

	@field:SerializedName("slot_id")
	val slotId: String,

	@field:SerializedName("cat_id")
	val catId: String,

	@field:SerializedName("patient_name")
	val patientName: String,

	@field:SerializedName("consult_type")
	val consultType: String,

	@field:SerializedName("time")
	val time: String,

	@field:SerializedName("auth_token")
	val authToken: String,

	@field:SerializedName("coupon_discount")
	val couponDiscount: Int,

	@field:SerializedName("family_member_id")
	val familyMemberId: String,

	@field:SerializedName("wallet_amount")
	val walletAmount: String,

	@field:SerializedName("language")
	val language: String,

	@field:SerializedName("free_booking_status")
	val freeBookingStatus: String
)