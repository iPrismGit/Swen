package com.iprism.medrayder.models.pharmacies

import com.google.gson.annotations.SerializedName

data class PharmaciesRequest(

	@field:SerializedName("search")
	val search: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("lon")
	val lon: String,

	@field:SerializedName("language")
	val language: String,

	@field:SerializedName("page")
	val page: Int,

	@field:SerializedName("auth_token")
	val authToken: String,

	@field:SerializedName("lat")
	val lat: String
)