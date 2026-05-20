package com.iprism.swen.models.hospitalmedicneongoing

import com.google.gson.annotations.SerializedName

data class BookingsItem(

	@field:SerializedName("booking_id")
	val bookingId: String,

	@field:SerializedName("accept_status")
	val acceptStatus: String,

	@field:SerializedName("created_on")
	val createdOn: String,

	@field:SerializedName("booking_type")
	val bookingType: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("order_type")
	val orderType: String,

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("hospital_name")
	val hospitalName: String,

	@field:SerializedName("booking_status")
	val bookingStatus: String,


	@field:SerializedName("products")
	val products: List<ProductsItem>
)