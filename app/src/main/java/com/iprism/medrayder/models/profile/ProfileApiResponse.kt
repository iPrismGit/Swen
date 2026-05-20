package com.iprism.medrayder.models.profile

import com.google.gson.annotations.SerializedName

data class ProfileApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)