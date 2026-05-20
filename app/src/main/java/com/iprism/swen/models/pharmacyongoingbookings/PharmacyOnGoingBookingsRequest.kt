package com.iprism.swen.models.pharmacyongoingbookings

import com.google.gson.annotations.SerializedName

data class PharmacyOnGoingBookingsRequest(

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("language")
	val language: String,

	@field:SerializedName("page")
	val page: Int,

	@field:SerializedName("auth_token")
	val authToken: String
)