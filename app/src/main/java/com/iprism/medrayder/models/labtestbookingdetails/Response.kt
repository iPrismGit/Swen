package com.iprism.medrayder.models.labtestbookingdetails

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Response(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("test_status")
	val testStatus: String,

	@field:SerializedName("reports")
	val reports: ArrayList<ReportsItem>,

	@field:SerializedName("packages")
	val packages: List<Package>,

	@field:SerializedName("agent")
	val agent: Agent,

	@field:SerializedName("address")
	val address: Address,

	@field:SerializedName("mobile")
	val mobile: Long,

	@field:SerializedName("address_id")
	val addressId: Int,

	@field:SerializedName("open_time")
	val openTime: String,

	@field:SerializedName("booking_status")
	val bookingStatus: String,

	@field:SerializedName("close_time")
	val closeTime: String,

	@field:SerializedName("lab_test_name")
	val labTestName: String,

	@field:SerializedName("booking_id")
	val bookingId: String,

	@field:SerializedName("accept_status")
	val acceptStatus: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("logo")
	val logo: String,

	@field:SerializedName("family_members")
	val familyMembers: List<FamilyMembersItem>,

	@field:SerializedName("booking_type")
	val bookingType: String,

	@field:SerializedName("location")
	val location: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("lab_test_id")
	val labTestId: Int,

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("time")
	val time: String,

	@field:SerializedName("fee")
	val fee: String,

	@field:SerializedName("consultation_fee")
	val consultationFee: String,

	@field:SerializedName("coupon_name")
	val couponName: String,

	@field:SerializedName("coupon_id")
	val couponId: String,

	@field:SerializedName("coupon_percentage")
	val couponPercentage: String,

	@field:SerializedName("coupon_discount")
	val couponDiscount: String,

	@field:SerializedName("flat_discount")
	val flatDiscount: String,

	@field:SerializedName("payment_type")
	val paymentType: String,

	@field:SerializedName("transaction_id")
	val transactionId: String,

	@field:SerializedName("lab_name")
	val labName: String,

	@field:SerializedName("fasting")
	val fasting: String,
) : Serializable