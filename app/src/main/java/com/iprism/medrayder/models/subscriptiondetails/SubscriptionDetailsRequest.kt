package com.iprism.medrayder.models.subscriptiondetails

import com.google.gson.annotations.SerializedName

data class SubscriptionDetailsRequest(

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("language")
	val language: String,

	@field:SerializedName("auth_token")
	val authToken: String
)