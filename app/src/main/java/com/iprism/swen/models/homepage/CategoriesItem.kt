package com.iprism.swen.models.homepage

import com.google.gson.annotations.SerializedName

data class CategoriesItem(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String
)