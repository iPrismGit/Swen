package com.iprism.swen.models.hospitals

import com.google.gson.annotations.SerializedName

data class HospitalResponse(

	@field:SerializedName("main_data")
	val mainData: List<MainDataItem>,

	@field:SerializedName("pagination")
	val pagination: Pagination
)