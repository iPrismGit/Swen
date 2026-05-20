package com.iprism.medrayder.models.hospitalmedicineproducts

import com.google.gson.annotations.SerializedName

data class HospitalMedicineProductsRequest(

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("cat_id")
	val catId: Int,

	@field:SerializedName("language")
	val language: String,

	@field:SerializedName("page")
	val page: Int,

	@field:SerializedName("auth_token")
	val authToken: String,

	@field:SerializedName("main_data_id")
	val mainDataId: String
)