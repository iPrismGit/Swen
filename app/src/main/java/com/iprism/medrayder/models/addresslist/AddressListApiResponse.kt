package com.iprism.medrayder.models.addresslist

import com.google.gson.annotations.SerializedName

data class AddressListApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)