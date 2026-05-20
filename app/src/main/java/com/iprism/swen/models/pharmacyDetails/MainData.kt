package com.iprism.swen.models.pharmacyDetails

import com.google.gson.annotations.SerializedName
import com.iprism.swen.models.hospitaldetails.ImagesItem

data class MainData(

	@field:SerializedName("home_delivery")
	val homeDelivery: String,

	@field:SerializedName("images")
	val images: List<ImagesItem>,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("mobile")
	val mobile: Long,

	@field:SerializedName("logo")
	val logo: String,

	@field:SerializedName("open_time")
	val openTime: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("location")
	val location: String,

	@field:SerializedName("lon")
	val lon: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("close_time")
	val closeTime: String,

	@field:SerializedName("lat")
	val lat: String,

	@field:SerializedName("pharmacy_categories")
	val pharmacyCategories: List<PharmacyCategory>
)