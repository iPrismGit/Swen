package com.iprism.medrayder.models.hospitalmedicinebooking

import com.google.gson.annotations.SerializedName

data class HospitalMedicineBookingRequest(

	@field:SerializedName("transaction_id")
	val transactionId: String,

	@field:SerializedName("consultation_fee")
	val consultationFee: String,

	@field:SerializedName("coupon_percentage")
	val couponPercentage: String,

	@field:SerializedName("fee")
	val fee: String,

	@field:SerializedName("address_id")
	val addressId: Int,

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

	@field:SerializedName("cat_id")
	val catId: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("auth_token")
	val authToken: String,

	@field:SerializedName("order_type")
	val orderType: String,

	@field:SerializedName("coupon_discount")
	val couponDiscount: String,

	@field:SerializedName("wallet_amount")
	val walletAmount: String
)