package com.iprism.swen.models.onlinedoctorbookingdetails

import com.google.gson.annotations.SerializedName

data class DatesItem(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("format_date")
	val formatDate: String,

	@field:SerializedName("convert_date")
	val convertDate: String
)