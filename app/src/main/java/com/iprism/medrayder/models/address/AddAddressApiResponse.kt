package com.iprism.medrayder.models.address

import com.google.gson.annotations.SerializedName

data class AddAddressApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)