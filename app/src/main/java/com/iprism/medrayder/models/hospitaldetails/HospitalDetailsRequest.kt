package com.iprism.medrayder.models.hospitaldetails

import com.google.gson.annotations.SerializedName

data class HospitalDetailsRequest(

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("lon")
	val lon: String,

	@field:SerializedName("language")
	val language: String,

	@field:SerializedName("auth_token")
	val authToken: String,

	@field:SerializedName("main_data_id")
	val mainDataId: Int,

	@field:SerializedName("lat")
	val lat: String
)