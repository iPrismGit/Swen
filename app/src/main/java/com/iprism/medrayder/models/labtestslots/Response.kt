package com.iprism.medrayder.models.labtestslots

import com.google.gson.annotations.SerializedName
import com.iprism.medrayder.models.diagnostictimings.Price
import com.iprism.medrayder.models.onlinedoctorbookingdetails.DatesItem
import com.iprism.medrayder.models.onlinedoctorbookingdetails.FamilyMembersItem
import com.iprism.medrayder.models.onlinedoctorbookingdetails.Slots

data class Response(

	@field:SerializedName("main_data")
	val mainData: MainDataItem,

	@field:SerializedName("dates")
	val dates: List<DatesItem>,

	@field:SerializedName("family_members")
	val familyMembers: ArrayList<FamilyMembersItem>,

	@field:SerializedName("slots")
	val slots: Slots,

	@field:SerializedName("prices")
	val prices: ArrayList<Price>,

	@field:SerializedName("name")
	val name: String,

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

	@field:SerializedName("flat_discount")
	val flatDiscount: String,

	@field:SerializedName("coupon_id")
	val couponId: String,

	@field:SerializedName("address")
	val address: Address,

	@field:SerializedName("subscription_status")
	val subscriptionStatus: String,
)