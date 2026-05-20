package com.iprism.medrayder.models.pharmacyDetails

import com.google.gson.annotations.SerializedName

data class PharmacyDetailsRequest(

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("language")
	val language: String,

	@field:SerializedName("auth_token")
	val authToken: String,

	@field:SerializedName("main_data_id")
	val mainDataId: Int
)