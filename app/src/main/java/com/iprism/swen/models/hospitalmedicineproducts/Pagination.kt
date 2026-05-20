package com.iprism.swen.models.hospitalmedicineproducts

import com.google.gson.annotations.SerializedName

data class Pagination(

	@field:SerializedName("limit")
	val limit: Int,

	@field:SerializedName("total_pages")
	val totalPages: List<TotalPagesItem>,

	@field:SerializedName("current_page")
	val currentPage: Int
)