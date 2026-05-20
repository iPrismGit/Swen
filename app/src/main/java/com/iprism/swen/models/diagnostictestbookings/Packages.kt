package com.iprism.swen.models.diagnostictestbookings

import com.google.gson.annotations.SerializedName

data class Packages(

	@field:SerializedName("report_in")
	val reportIn: String,

	@field:SerializedName("package_name")
	val packageName: String,

	@field:SerializedName("fasting")
	val fasting: String,

	@field:SerializedName("package_id")
	val packageId: Int
)