package com.iprism.swen.models.profile

import com.google.gson.annotations.SerializedName

data class ProfileRequest(

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("language")
	val language: String,

	@field:SerializedName("auth_token")
	val authToken: String
)