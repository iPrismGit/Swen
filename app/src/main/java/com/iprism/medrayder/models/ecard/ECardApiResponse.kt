package com.iprism.medrayder.models.ecard

import com.google.gson.annotations.SerializedName

data class ECardApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class Response(

	@field:SerializedName("unique_id")
	val uniqueId: String,

	@field:SerializedName("to_date")
	val toDate: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("image")
	val image: String
)
