package com.iprism.swen.models.pharmacyproducts

import com.google.gson.annotations.SerializedName

data class TotalPagesItem(

	@field:SerializedName("page")
	val page: Int
)