package com.iprism.medrayder.models.labbooking

import com.google.gson.annotations.SerializedName

data class LabBookingRequest(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("transaction_id")
	val transactionId: String,

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("consultation_fee")
	val consultationFee: Int,

	@field:SerializedName("coupon_percentage")
	val couponPercentage: Int,

	@field:SerializedName("fee")
	val fee: String,

	@field:SerializedName("address_id")
	val addressId: Int,

	@field:SerializedName("count")
	val count: Int,

	@field:SerializedName("mobile")
	val mobile: String,

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

	@field:SerializedName("lab_test_id")
	val labTestId: Int,

	@field:SerializedName("coupon_discount")
	val couponDiscount: Int,

	@field:SerializedName("test_id")
	val testId: Int,

	@field:SerializedName("family_member_id")
	val familyMemberId: String,

	@field:SerializedName("wallet_amount")
	val walletAmount: Int,

	@field:SerializedName("language")
	val language: String,

	@field:SerializedName("flat_discount")
	val flatDiscount: String
)