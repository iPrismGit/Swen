package com.iprism.medrayder.models.pharmacyprescriptionbooking

import com.google.gson.annotations.SerializedName

data class PharmacyPrescriptionBookingRequest(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("pharmacy_id")
	val pharmacyId: Int,

	@field:SerializedName("address_id")
	val addressId: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("mobile")
	val mobile: String,

	@field:SerializedName("view_type")
	val viewType: String,

	@field:SerializedName("auth_token")
	val authToken: String,

	@field:SerializedName("order_type")
	val orderType: String,

	@field:SerializedName("language")
	val language: String
)