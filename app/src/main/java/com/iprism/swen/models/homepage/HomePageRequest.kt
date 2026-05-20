package com.iprism.swen.models.homepage

import com.google.gson.annotations.SerializedName

data class HomePageRequest(

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("language")
	val language: String,

	@field:SerializedName("auth_token")
	val authToken: String,

	@field:SerializedName("player_id")
	val token: String
)