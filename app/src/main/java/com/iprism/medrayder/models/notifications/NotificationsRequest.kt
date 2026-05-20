package com.iprism.medrayder.models.notifications

import com.google.gson.annotations.SerializedName

data class NotificationsRequest(

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("view_type")
	val viewType: String,

	@field:SerializedName("language")
	val language: String,

	@field:SerializedName("page")
	val page: Int,

	@field:SerializedName("auth_token")
	val authToken: String
)
