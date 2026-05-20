package com.iprism.swen.models.pharmacyproductcart

import com.google.gson.annotations.SerializedName

data class ReceiptDetails(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("mobile")
	val mobile: String
)