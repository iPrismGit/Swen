package com.iprism.swen.models.diagnostictimings

import com.google.gson.annotations.SerializedName

data class SubTestsItem(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int
)