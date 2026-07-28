package com.iprism.swen.models.symptomdoctorbookingdetails

import com.google.gson.annotations.SerializedName
import com.iprism.swen.models.onlinedoctorbookingdetails.DatesItem
import com.iprism.swen.models.onlinedoctorbookingdetails.FamilyMembersItem
import com.iprism.swen.models.onlinedoctorbookingdetails.TimesItem

data class SymptomDoctorBookingDetailsApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class Slots(

	@field:SerializedName("afternoon")
	val afternoon: List<TimesItem>,

	@field:SerializedName("evening")
	val evening: List<TimesItem>,

	@field:SerializedName("morning")
	val morning: List<TimesItem>
)

data class Response(

	@field:SerializedName("consultation_fee")
	val consultationFee: String,

	@field:SerializedName("available_status")
	val availableStatus: Int,

	@field:SerializedName("slots")
	val slots: Slots,

	@field:SerializedName("coupon_percentage")
	val couponPercentage: Int,

	@field:SerializedName("coupon_id")
	val couponId: Int,

	@field:SerializedName("mobile")
	val mobile: String,

	@field:SerializedName("family_members")
	val familyMembers: ArrayList<FamilyMembersItem>,

	@field:SerializedName("dates")
	val dates: List<DatesItem>,

	@field:SerializedName("coupon_discount")
	val couponDiscount: String
)