package com.iprism.swen.models.homeservices

import com.google.gson.annotations.SerializedName

data class HomeServicesApiResponse(

	@field:SerializedName("response")
	val response: List<ResponseItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class ResponseItem(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int
)
