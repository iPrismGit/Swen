package com.iprism.swen.models.hospitals

import com.google.gson.annotations.SerializedName

data class MainDataItem(

	@field:SerializedName("sub_sub_cat_id")
	val subSubCatId: Int,

	@field:SerializedName("sub_cat_id")
	val subCatId: Int,

	@field:SerializedName("cat_id")
	val catId: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("tagline")
	val tagline: String,

	@field:SerializedName("logo")
	val logo: String,

	@field:SerializedName("open_time")
	val openTime: String,

	@field:SerializedName("location")
	val location: String,

	@field:SerializedName("lon")
	val lon: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("close_time")
	val closeTime: String,

	@field:SerializedName("lat")
	val lat: String
)