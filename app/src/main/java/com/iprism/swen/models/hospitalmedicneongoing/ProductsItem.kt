package com.iprism.swen.models.hospitalmedicneongoing

import com.google.gson.annotations.SerializedName

data class ProductsItem(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("original_price")
	val originalPrice: String,

	@field:SerializedName("price")
	val price: String,

	@field:SerializedName("product_name")
	val productName: String
)