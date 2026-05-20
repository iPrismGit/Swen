package com.iprism.medrayder.models.hospitaldetails

import com.google.gson.annotations.SerializedName

data class MainDataItem(

	@field:SerializedName("images")
	val images: List<ImagesItem>,

	@field:SerializedName("sub_sub_cat_id")
	val subSubCatId: Int,

	@field:SerializedName("sub_cat_id")
	val subCatId: Int,

	@field:SerializedName("mobile")
	val mobile: Long,

	@field:SerializedName("open_time")
	val openTime: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("lon")
	val lon: String,

	@field:SerializedName("close_time")
	val closeTime: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("cat_id")
	val catId: Int,

	@field:SerializedName("tagline")
	val tagline: String,

	@field:SerializedName("logo")
	val logo: String,

	@field:SerializedName("location")
	val location: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("lat")
	val lat: String,

	@field:SerializedName("home_delivery")
	val homeDelivery: String,

	@field:SerializedName("specialities")
	val specialities: List<SpecialitiesItem>
)