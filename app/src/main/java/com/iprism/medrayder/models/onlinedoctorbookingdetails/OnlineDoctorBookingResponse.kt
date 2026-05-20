package com.iprism.medrayder.models.onlinedoctorbookingdetails

import com.google.gson.annotations.SerializedName

data class OnlineDoctorBookingResponse(

	@field:SerializedName("slots")
	val slots: Slots,

	@field:SerializedName("family_members")
	val familyMembers: ArrayList<FamilyMembersItem>,

	@field:SerializedName("dates")
	val dates: List<DatesItem>,

	@field:SerializedName("mobile")
	val mobile: String,

	@field:SerializedName("wallet_amount")
	val walletAmount: String,

	@field:SerializedName("consultation_fee")
	val consultationFee: String,

	@field:SerializedName("coupon_percentage")
	val couponPercentage: String,

	@field:SerializedName("coupon_discount")
	val couponDiscount: String,

	@field:SerializedName("coupon_id")
	val couponId: String,

	@field:SerializedName("free_booking_status")
	val freeBookingStatus: String,
)