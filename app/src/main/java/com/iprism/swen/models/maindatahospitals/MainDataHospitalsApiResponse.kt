package com.iprism.swen.models.maindatahospitals

import com.google.gson.annotations.SerializedName
import com.iprism.swen.models.hospitals.MainDataItem
import com.iprism.swen.models.hospitals.Pagination

data class MainDataHospitalsApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
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