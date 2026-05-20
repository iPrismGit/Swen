package com.iprism.medrayder.models.addfamilymembersub

import com.google.gson.annotations.SerializedName

data class AddFamilyMemberSubRequest(

	@field:SerializedName("transaction_id")
	val transactionId: String,

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("gender")
	val gender: String,

	@field:SerializedName("mobile")
	val mobile: String,

	@field:SerializedName("view_type")
	val viewType: String,

	@field:SerializedName("gst")
	val gst: String,

	@field:SerializedName("language")
	val language: String,

	@field:SerializedName("subscription_id")
	val subscriptionId: Int,

	@field:SerializedName("duration")
	val duration: String,

	@field:SerializedName("total_premium")
	val totalPremium: Int,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("total_amount")
	val totalAmount: String,

	@field:SerializedName("dob")
	val dob: String,

	@field:SerializedName("price")
	val price: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("blood_group")
	val bloodGroup: Int,

	@field:SerializedName("auth_token")
	val authToken: String,

	@field:SerializedName("family_members_count")
	val familyMembersCount: Int,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("sub_name")
	val subscriptionName: String,

	@field:SerializedName("coverage_category")
	val coverageCategory: String
)
