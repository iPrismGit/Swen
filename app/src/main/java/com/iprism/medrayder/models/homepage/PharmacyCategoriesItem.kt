package com.iprism.medrayder.models.homepage

import com.google.gson.annotations.SerializedName

data class PharmacyCategoriesItem(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int
)