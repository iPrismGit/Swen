package com.iprism.swen.models.pharmacyproducts

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("main_data")
	val mainData: List<MainDataItem>,

	@field:SerializedName("pagination")
	val pagination: Pagination,

	@field:SerializedName("fee")
	val fee: Int,

	@field:SerializedName("count")
	val count: Int
)