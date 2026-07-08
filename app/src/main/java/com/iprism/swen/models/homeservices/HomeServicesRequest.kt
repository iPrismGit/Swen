package com.iprism.swen.models.homeservices

import com.google.gson.annotations.SerializedName

data class HomeServicesRequest(

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("cat_id")
	val catId: Int,

	@field:SerializedName("view_type")
	val viewType: String,

	@field:SerializedName("auth_token")
	val authToken: String
)
