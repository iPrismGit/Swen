package com.iprism.medrayder.models.hospitaldiagnosticbooking

import com.google.gson.annotations.SerializedName

data class HospitalDiagnosticBookingRequest(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("transaction_id")
	val transactionId: String,

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("consultation_fee")
	val consultationFee: String,

	@field:SerializedName("coupon_percentage")
	val couponPercentage: String,

	@field:SerializedName("fee")
	val fee: String,

	@field:SerializedName("count")
	val count: Int,

	@field:SerializedName("mobile")
	val mobile: String,

	@field:SerializedName("main_data_id")
	val mainDataId: Int,

	@field:SerializedName("coupon_name")
	val couponName: String,

	@field:SerializedName("payment_type")
	val paymentType: String,

	@field:SerializedName("coupon_id")
	val couponId: Int,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("slot_id")
	val slotId: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("time")
	val time: String,

	@field:SerializedName("auth_token")
	val authToken: String,

	@field:SerializedName("coupon_discount")
	val couponDiscount: String,

	@field:SerializedName("test_id")
	val testId: Int,

	@field:SerializedName("family_member_id")
	val familyMemberId: String,

	@field:SerializedName("wallet_amount")
	val walletAmount: String,

	@field:SerializedName("language")
	val language: String
)