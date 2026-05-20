package com.iprism.swen.models.medlocker

import com.google.gson.annotations.SerializedName

data class MedLockerRequest(

	@field:SerializedName("image")
	val image: List<String>,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("view_type")
	val viewType: String,

	@field:SerializedName("alert_date")
	val alertDate: String,

	@field:SerializedName("auth_token")
	val authToken: String
)