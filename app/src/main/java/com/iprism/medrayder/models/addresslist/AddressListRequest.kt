package com.iprism.medrayder.models.addresslist

import com.google.gson.annotations.SerializedName

data class AddressListRequest(

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("auth_token")
	val authToken: String,


	@field:SerializedName("language")
	val language: String
)