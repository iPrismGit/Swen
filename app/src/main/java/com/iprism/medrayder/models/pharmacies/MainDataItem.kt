package com.iprism.medrayder.models.pharmacies

import com.google.gson.annotations.SerializedName

data class MainDataItem(

	@field:SerializedName("home_delivery")
	val homeDelivery: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("logo")
	val logo: String,

	@field:SerializedName("open_time")
	val openTime: String,

	@field:SerializedName("location")
	val location: String,

	@field:SerializedName("lon")
	val lon: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("close_time")
	val closeTime: String,

	@field:SerializedName("lat")
	val lat: String
)