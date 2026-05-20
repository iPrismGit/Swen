package com.iprism.swen.models.familymembers

import com.google.gson.annotations.SerializedName

data class FamilyMembersRequest(

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("language")
	val language: String,

	@field:SerializedName("auth_token")
	val authToken: String
)