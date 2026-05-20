package com.iprism.swen.models.userdropdowns

import com.google.gson.annotations.SerializedName

data class CategoriesItem(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int
)