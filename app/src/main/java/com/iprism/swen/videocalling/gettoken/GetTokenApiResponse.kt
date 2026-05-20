package com.iprism.medrayderonlinedoctor.videocalling.gettoken

import com.google.gson.annotations.SerializedName

data class GetTokenApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)