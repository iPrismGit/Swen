package com.iprism.swen.models.filters

import com.google.gson.annotations.SerializedName

data class FiltersApiResponse1(

	@field:SerializedName("search")
	val search: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("view_type")
	val viewType: String,

	@field:SerializedName("lon")
	val lon: String,

	@field:SerializedName("language")
	val language: String,

	@field:SerializedName("page")
	val page: Int,

	@field:SerializedName("filters")
	val filters: List<FiltersItem>,

	@field:SerializedName("auth_token")
	val authToken: String,

	@field:SerializedName("lat")
	val lat: String
)

data class FiltersItem(

	@field:SerializedName("cat_id")
	val catId: Int,

	@field:SerializedName("speciality_ids")
	val specialityIds: List<Int>
)
