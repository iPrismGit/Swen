package com.iprism.swen.models.ambulancebooking

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("address")
	val address: Address,

	@field:SerializedName("user_details")
	val userDetails: UserDetails
)