package com.iprism.medrayder.models.contactus

import com.google.gson.annotations.SerializedName

data class ContactUsApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class Response(

	@field:SerializedName("mobile")
	val mobile: String
)
