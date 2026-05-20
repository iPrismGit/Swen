package com.iprism.swen.models.hospitalambulancebookings

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PickupLocation(

	@field:SerializedName("pincode")
	val pincode: Int,

	@field:SerializedName("default_address")
	val defaultAddress: Int,

	@field:SerializedName("colony")
	val colony: String,

	@field:SerializedName("address_type")
	val addressType: String,

	@field:SerializedName("hno")
	val hno: String,

	@field:SerializedName("state")
	val state: String,

	@field:SerializedName("landmark")
	val landmark: String,

	@field:SerializedName("building_no")
	val buildingNo: String,

	@field:SerializedName("address")
	val address: String,

	@field:SerializedName("lat")
	val lat: Double,

	@field:SerializedName("lon")
	val lon: Double,
) : Serializable