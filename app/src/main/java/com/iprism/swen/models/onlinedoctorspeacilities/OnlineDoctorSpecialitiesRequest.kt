package com.iprism.swen.models.onlinedoctorspeacilities

import com.google.gson.annotations.SerializedName

data class OnlineDoctorSpecialitiesRequest(

	@field:SerializedName("user_id")
	val userId: String,

	@field:SerializedName("language")
	val language: String,

	@field:SerializedName("auth_token")
	val authToken: String
)