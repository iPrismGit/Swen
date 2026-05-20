package com.iprism.swen.models.homepage

import com.google.gson.annotations.SerializedName

data class BannersItem(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("id")
	val id: Int
)