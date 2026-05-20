package com.iprism.medrayder.models.hospitals

import com.google.gson.annotations.SerializedName
import com.iprism.medrayder.models.filters.FiltersItem

data class HospitalsRequest(

	@field:SerializedName("search")
	val search: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("lon")
	val lon: String,

	@field:SerializedName("language")
	val language: String,

	@field:SerializedName("page")
	val page: Int,

	@field:SerializedName("auth_token")
	val authToken: String,

	@field:SerializedName("lat")
	val lat: String,

	@field:SerializedName("view_type")
	val viewType: String,

	@field:SerializedName("filters")
	val filters: List<FiltersItem>
)