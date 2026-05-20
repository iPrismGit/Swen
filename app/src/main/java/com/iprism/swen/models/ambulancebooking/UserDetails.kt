package com.iprism.swen.models.ambulancebooking

import com.google.gson.annotations.SerializedName

data class UserDetails(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("mobile")
	val mobile: String,

	@field:SerializedName("id")
	val id: Int
)