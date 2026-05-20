package com.iprism.swen.models.labtestbookings

import com.google.gson.annotations.SerializedName

data class TotalPagesItem(

	@field:SerializedName("page")
	val page: Int
)