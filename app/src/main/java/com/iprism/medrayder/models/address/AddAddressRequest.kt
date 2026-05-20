package com.iprism.medrayder.models.address

import com.google.gson.annotations.SerializedName

data class AddAddressRequest(

	@field:SerializedName("pincode")
	val pincode: String,

	@field:SerializedName("address")
	val address: String,

	@field:SerializedName("address_type")
	val addressType: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("hno")
	val hno: String,

	@field:SerializedName("lon")
	val lon: String,

	@field:SerializedName("building_no")
	val buildingNo: String,

	@field:SerializedName("state")
	val state: String,

	@field:SerializedName("auth_token")
	val authToken: String,

	@field:SerializedName("landmark")
	val landmark: String,

	@field:SerializedName("lat")
	val lat: String,

	@field:SerializedName("city")
	val city: String
)