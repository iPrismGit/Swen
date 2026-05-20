package com.iprism.medrayder.models.userdropdowns

import com.google.gson.annotations.SerializedName

data class UserDropDownApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)