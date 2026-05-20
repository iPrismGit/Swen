package com.iprism.medrayder.models.addresslist

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AddressItem(

	@field:SerializedName("modified_on")
	val modifiedOn: Any,

	@field:SerializedName("pincode")
	val pincode: Int,

	@field:SerializedName("address")
	val address: String,

	@field:SerializedName("address_type")
	val addressType: String,

	@field:SerializedName("hno")
	val hno: String,

	@field:SerializedName("lon")
	val lon: String,

	@field:SerializedName("building_no")
	val buildingNo: String,

	@field:SerializedName("default_address")
	val defaultAddress: Int,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("created_on")
	val createdOn: String,

	@field:SerializedName("delete_status")
	val deleteStatus: Int,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("state")
	val state: String,

	@field:SerializedName("landmark")
	val landmark: String,

	@field:SerializedName("lat")
	val lat: String,

	@field:SerializedName("status")
	val status: Int
) : Serializable