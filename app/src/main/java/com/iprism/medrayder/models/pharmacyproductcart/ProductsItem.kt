package com.iprism.medrayder.models.pharmacyproductcart

import com.google.gson.annotations.SerializedName

data class ProductsItem(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("pharmacy_id")
	val pharmacyId: Int,

	@field:SerializedName("price")
	val price: Int,

	@field:SerializedName("product_id")
	val productId: Int,

	@field:SerializedName("cat_id")
	val catId: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("cart_quantity")
	val cartQuantity: Int,

	@field:SerializedName("original_price")
	val originalPrice: Int
)