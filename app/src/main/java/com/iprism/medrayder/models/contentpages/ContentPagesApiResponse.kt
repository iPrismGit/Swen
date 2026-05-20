package com.iprism.medrayder.models.contentpages

import com.google.gson.annotations.SerializedName

data class ContentPagesApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class Response(

	@field:SerializedName("privacy")
	val privacy: String,

	@field:SerializedName("terms")
	val terms: String,

	@field:SerializedName("refund")
	val refund: String,

	@field:SerializedName("about_us")
	val aboutUs: String
)
