package com.iprism.medrayder.models.filters

import com.google.gson.annotations.SerializedName

data class FiltersApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class SpecialitiesItem(

	@field:SerializedName("speciality_name")
	val specialityName: String,

	@field:SerializedName("speciality_id")
	val specialityId: Int,

	var status: Boolean = false
)

data class DetailsItem(

	@field:SerializedName("category_name")
	val categoryName: String,

	@field:SerializedName("category_id")
	val categoryId: Int,

	@field:SerializedName("specialities")
	val specialities: ArrayList<SpecialitiesItem>
)

data class Response(

	@field:SerializedName("details")
	val details: List<DetailsItem>
)
