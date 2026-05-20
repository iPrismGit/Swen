package com.iprism.medrayder.models.wallethistory

import com.google.gson.annotations.SerializedName

data class ResponseItem(

	@field:SerializedName("amount")
	val amount: Int,

	@field:SerializedName("created_on")
	val createdOn: String,

	@field:SerializedName("booking_type")
	val bookingType: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("type")
	val type: String
)