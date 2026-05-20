package com.iprism.medrayder.models.hospitalmedicineproducts

import com.google.gson.annotations.SerializedName
import com.iprism.medrayder.models.pharmacyproducts.MainDataItem

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