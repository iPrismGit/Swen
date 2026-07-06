package com.iprism.ecmuser.models.healthmedia

import com.google.gson.annotations.SerializedName

data class HealthMediaApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class Pagination(

	@field:SerializedName("limit")
	val limit: Int,

	@field:SerializedName("total_pages")
	val totalPages: List<TotalPagesItem>,

	@field:SerializedName("current_page")
	val currentPage: Int
)

data class MainDataItem(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("id")
	val id: String
)

data class TotalPagesItem(

	@field:SerializedName("page")
	val page: Int
)

data class Response(

	@field:SerializedName("main_data")
	val mainData: List<MainDataItem>,

	@field:SerializedName("pagination")
	val pagination: Pagination
)
