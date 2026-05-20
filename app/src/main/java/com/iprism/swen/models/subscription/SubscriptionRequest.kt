package com.iprism.swen.models.subscription

import com.google.gson.annotations.SerializedName

data class SubscriptionRequest(

	@field:SerializedName("transaction_id")
	val transactionId: String,

	@field:SerializedName("family_id")
	val familyId: String,

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
	val userId: String,

	@field:SerializedName("total_amount")
	val totalAmount: String,

	@field:SerializedName("price")
	val price: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("auth_token")
	val authToken: String,

	@field:SerializedName("family_members_count")
	val familyMembersCount: Int
)