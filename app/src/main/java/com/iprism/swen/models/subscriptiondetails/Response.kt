package com.iprism.swen.models.subscriptiondetails

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("subscription_id")
	val subscriptionId: Int,

	@field:SerializedName("duration")
	val duration: String,

	@field:SerializedName("total_premium")
	val totalPremium: Int,

	@field:SerializedName("from_date")
	val fromDate: String,

	@field:SerializedName("to_date")
	val toDate: String,

	@field:SerializedName("total_amount")
	val totalAmount: String,

	@field:SerializedName("price")
	val price: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("count")
	val count: Int,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("invoice")
	val invoice: String
)