package com.iprism.swen.models.hospitaldoctors

import com.google.gson.annotations.SerializedName

data class HospitalDoctorsRequest(

	@field:SerializedName("user_id")
	val userId: String,

	@field:SerializedName("speciality_id")
	val specialityId: String,

	@field:SerializedName("language")
	val language: String,

	@field:SerializedName("page")
	val page: String,

	@field:SerializedName("auth_token")
	val authToken: String,

	@field:SerializedName("main_data_id")
	val mainDataId: String
)