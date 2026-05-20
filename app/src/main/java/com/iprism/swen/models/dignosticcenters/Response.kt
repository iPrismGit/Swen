package com.iprism.swen.models.dignosticcenters

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("main_data")
	val mainData: List<MainDataItem>,

	@field:SerializedName("pagination")
	val pagination: Pagination
)