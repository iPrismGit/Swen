package com.iprism.swen.models.addfamilymember

import com.google.gson.annotations.SerializedName

data class AddFamilyMemberApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)