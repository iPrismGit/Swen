package com.iprism.swen.models.hospitalmedicineaddtocart

import com.google.gson.annotations.SerializedName

data class HospitalMedineProductsAddToCartRequest(

	@field:SerializedName("original_price")
	val originalPrice: Int,

	@field:SerializedName("cart_id")
	val cartId: Int,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("price")
	val price: Int,

	@field:SerializedName("product_id")
	val productId: Int,

	@field:SerializedName("cat_id")
	val catId: Int,

	@field:SerializedName("view_type")
	val viewType: String,

	@field:SerializedName("auth_token")
	val authToken: String,

	@field:SerializedName("main_data_id")
	val mainDataId: String
)