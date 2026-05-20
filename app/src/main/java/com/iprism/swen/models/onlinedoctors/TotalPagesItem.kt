package com.iprism.swen.models.onlinedoctors

import com.google.gson.annotations.SerializedName

data class TotalPagesItem(

	@field:SerializedName("page")
	val page: String
)