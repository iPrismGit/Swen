package com.iprism.medrayder.models.pharmacyproducts

import com.google.gson.annotations.SerializedName

data class PharmacyProductsRequest(

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("pharmacy_id")
	val pharmacyId: Int,

	@field:SerializedName("cat_id")
	val catId: Int,

	@field:SerializedName("language")
	val language: String,

	@field:SerializedName("page")
	val page: Int,

	@field:SerializedName("auth_token")
	val authToken: String,

	@field:SerializedName("search")
	val search: String
)