package com.iprism.medrayder.models.hospitaldetails

import com.google.gson.annotations.SerializedName

data class SpecialitiesItem(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String
)