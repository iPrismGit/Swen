package com.iprism.swen.models.pharmacyproducts

import com.google.gson.annotations.SerializedName

data class MainDataItem(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("cart_id")
	val cartId: Int,

	@field:SerializedName("price")
	val price: Int,

	@field:SerializedName("discount_price")
	val discountPrice: Int,

	@field:SerializedName("cat_id")
	val catId: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("main_data_id")
	val mainDataId: Int,

	@field:SerializedName("cart_quantity")
	var cartQuantity: Int
)