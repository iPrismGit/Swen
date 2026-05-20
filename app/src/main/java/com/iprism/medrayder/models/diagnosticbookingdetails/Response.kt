package com.iprism.medrayder.models.diagnosticbookingdetails

import com.google.gson.annotations.SerializedName
import com.iprism.medrayder.models.labtestbookingdetails.Address
import com.iprism.medrayder.models.labtestbookingdetails.FamilyMembersItem
import com.iprism.medrayder.models.labtestbookingdetails.ReportsItem

data class Response(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("reports")
	val reports: ArrayList<ReportsItem>,

	@field:SerializedName("coupon_percentage")
	val couponPercentage: Int,

	@field:SerializedName("fee")
	val fee: Int,

	@field:SerializedName("total_test_count")
	val totalTestCount: Int,

	@field:SerializedName("coupon_name")
	val couponName: String,

	@field:SerializedName("booking_id")
	val bookingId: String,

	@field:SerializedName("coupon_id")
	val couponId: Int,

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

	@field:SerializedName("mobile")
	val mobile: Long,

	@field:SerializedName("booking_status")
	val bookingStatus: String,

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

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("booking_type")
	val bookingType: String,

	@field:SerializedName("time")
	val time: String,

	@field:SerializedName("coupon_discount")
	val couponDiscount: Int,

	@field:SerializedName("flat_discount")
	val flatDiscount : String,

	@field:SerializedName("family_member_id")
	val familyMemberId: String,

	@field:SerializedName("diagnostic_name")
	val diagnosticName: String,

	@field:SerializedName("location")
	val location: String,

	@field:SerializedName("logo")
	val logo: String,

	@field:SerializedName("lat")
	val lat: String,

	@field:SerializedName("lon")
	val lon: String,
)