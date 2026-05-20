package com.iprism.medrayder.models.labtestbookings

import com.google.gson.annotations.SerializedName

data class Package(

	@field:SerializedName("report_in")
	val reportIn: String,

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("package_name")
	val packageName: String,

	@field:SerializedName("fasting")
	val fasting: String,

	@field:SerializedName("package_id")
	val packageId: String
)