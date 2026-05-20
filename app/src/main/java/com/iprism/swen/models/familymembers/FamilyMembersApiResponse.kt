package com.iprism.swen.models.familymembers

import com.google.gson.annotations.SerializedName

data class FamilyMembersApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)