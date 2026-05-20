package com.iprism.medrayder.models.pharmacyprescriptionbooking

import com.google.gson.annotations.SerializedName

data class Address(

	@field:SerializedName("pincode")
	val pincode: String,

	@field:SerializedName("default_address")
	val defaultAddress: Int,

	@field:SerializedName("colony")
	val colony: String,

	@field:SerializedName("address_type")
	val addressType: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("hno")
	val hno: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("state")
	val state: String,

	@field:SerializedName("landmark")
	val landmark: String,

	@field:SerializedName("lat")
	val lat: String,

	@field:SerializedName("lon")
	val lon: String,

	@field:SerializedName("building_no")
	val buildingNo: String,

	@field:SerializedName("address")
	val address: String
)