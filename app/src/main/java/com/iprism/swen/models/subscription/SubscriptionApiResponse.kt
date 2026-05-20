package com.iprism.swen.models.subscription

import com.google.gson.annotations.SerializedName

data class SubscriptionApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)