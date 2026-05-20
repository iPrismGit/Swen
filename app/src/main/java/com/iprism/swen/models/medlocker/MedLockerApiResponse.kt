package com.iprism.swen.models.medlocker

import com.google.gson.annotations.SerializedName

data class MedLockerApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)