package com.iprism.medrayder.models.ecard

import com.google.gson.annotations.SerializedName

data class ECardRequest(

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("auth_token")
	val authToken: String
)
