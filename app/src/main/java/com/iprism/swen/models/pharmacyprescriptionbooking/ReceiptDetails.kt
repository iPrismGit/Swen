package com.iprism.swen.models.pharmacyprescriptionbooking

import com.google.gson.annotations.SerializedName

data class ReceiptDetails(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("mobile")
	val mobile: String
)