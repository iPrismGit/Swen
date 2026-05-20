package com.iprism.medrayder.models.homepage

import com.google.gson.annotations.SerializedName

data class HomePageApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)