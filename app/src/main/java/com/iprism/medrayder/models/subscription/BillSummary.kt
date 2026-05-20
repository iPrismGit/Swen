package com.iprism.medrayder.models.subscription

import com.google.gson.annotations.SerializedName

data class BillSummary(

	@field:SerializedName("total_premium")
	val totalPremium: Int,

	@field:SerializedName("total_amount")
	val totalAmount: String,

	@field:SerializedName("price")
	val price: Int,

	@field:SerializedName("gst")
	val gst: String,

	@field:SerializedName("family_members_count")
	val familyMembersCount: Int
)