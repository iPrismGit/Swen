package com.iprism.swen.models.hospitaldiagnosticbookingdetails

import com.google.gson.annotations.SerializedName
import com.iprism.swen.models.labtestbookingdetails.FamilyMembersItem
import com.iprism.swen.models.labtestbookingdetails.ReportsItem

data class Response(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("reports")
	val reports: ArrayList<ReportsItem>,

	@field:SerializedName("coupon_percentage")
	val couponPercentage: Int,

	@field:SerializedName("fee")
	val fee: Int,

	@field:SerializedName("open_time")
	val openTime: String,

	@field:SerializedName("coupon_name")
	val couponName: String,

	@field:SerializedName("booking_id")
	val bookingId: String,

	@field:SerializedName("coupon_id")
	val couponId: Int,

	@field:SerializedName("diagnostic_name")
	val diagnosticName: String,

	@field:SerializedName("logo")
	val logo: String,

	@field:SerializedName("family_members")
	val familyMembers: List<FamilyMembersItem>,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("transaction_id")
	val transactionId: String,

	@field:SerializedName("test_status")
	val testStatus: String,

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("consultation_fee")
	val consultationFee: Int,

	@field:SerializedName("count")
	val count: Int,

	@field:SerializedName("booking_status")
	val bookingStatus: String,

	@field:SerializedName("close_time")
	val closeTime: String,

	@field:SerializedName("packages")
	val packages: List<PackagesItem>,

	@field:SerializedName("diagnostic_id")
	val diagnosticId: Int,

	@field:SerializedName("accept_status")
	val acceptStatus: String,

	@field:SerializedName("payment_type")
	val paymentType: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("slot_id")
	val slotId: Int,

	@field:SerializedName("booking_type")
	val bookingType: String,

	@field:SerializedName("location")
	val location: String,

	@field:SerializedName("time")
	val time: String,

	@field:SerializedName("coupon_discount")
	val couponDiscount: Int,

	@field:SerializedName("family_member_id")
	val familyMemberId: String,

	@field:SerializedName("lat")
	val lat: String,

	@field:SerializedName("lon")
	val lon: String,

	@field:SerializedName("package_test_name")
	val packageTestName: String,
)