package com.iprism.swen.models.hospitalmedbookingdetails

import com.google.gson.annotations.SerializedName

data class ProductsItem(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("product_name")
	val productName: String,

	@field:SerializedName("hospital_name")
	val hospitalName: String,

	@field:SerializedName("price")
	val price: String,

	@field:SerializedName("original_price")
	val originalPrice: String,

	@field:SerializedName("cart_quantity")
	val cartQuantity: String
)