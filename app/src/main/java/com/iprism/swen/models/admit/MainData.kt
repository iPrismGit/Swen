package com.iprism.swen.models.admit

import com.google.gson.annotations.SerializedName

data class MainData(

	@field:SerializedName("cat_id")
	val catId: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("mobile")
	val mobile: Long,

	@field:SerializedName("logo")
	val logo: String,

	@field:SerializedName("tagline")
	val tagline: String,

	@field:SerializedName("location")
	val location: String,

	@field:SerializedName("lon")
	val lon: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("lat")
	val lat: String
)