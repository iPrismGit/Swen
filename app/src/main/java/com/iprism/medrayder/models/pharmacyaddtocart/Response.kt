package com.iprism.medrayder.models.pharmacyaddtocart

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("fee")
	val fee: Int,

	@field:SerializedName("count")
	val count: Int
)