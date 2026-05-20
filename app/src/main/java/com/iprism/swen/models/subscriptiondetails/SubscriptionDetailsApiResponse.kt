package com.iprism.swen.models.subscriptiondetails

import com.google.gson.annotations.SerializedName

data class SubscriptionDetailsApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)