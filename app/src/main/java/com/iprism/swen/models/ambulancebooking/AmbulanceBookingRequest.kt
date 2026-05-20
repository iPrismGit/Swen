package com.iprism.swen.models.ambulancebooking

import com.google.gson.annotations.SerializedName

data class AmbulanceBookingRequest(

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("address_id")
	val addressId: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("mobile")
	val mobile: String,

	@field:SerializedName("view_type")
	val viewType: String,

	@field:SerializedName("auth_token")
	val authToken: String,

	@field:SerializedName("main_data_id")
	val mainDataId: Int,

	@field:SerializedName("language")
	val language: String
)