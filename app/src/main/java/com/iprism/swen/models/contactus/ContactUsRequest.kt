package com.iprism.swen.models.contactus

import com.google.gson.annotations.SerializedName

data class ContactUsRequest(

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("mobile")
	val mobile: String,

	@field:SerializedName("view_type")
	val viewType: String,

	@field:SerializedName("auth_token")
	val authToken: String,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("email")
	val email: String
)
