package com.iprism.swen.models.onlinedoctors

import com.google.gson.annotations.SerializedName

data class OnlineDoctorRequest(

	@field:SerializedName("user_id")
	val userId: String,

	@field:SerializedName("speciality_id")
	val specialityId: String,

	@field:SerializedName("language")
	val language: String,

	@field:SerializedName("page")
	val page: String,

	@field:SerializedName("auth_token")
	val authToken: String
)